(ns trader.broker.auth
  (:require
   [trader.db.core :refer [*db*] :as db]
   [clj-http.client :as client]
   [cheshire.core :refer [generate-string parse-string]]
   [clojure.java.jdbc :as jdbc])
  )

(def base-url "https://api.tdameritrade.com/v1")

(defn get-api-key []
  (jdbc/with-db-transaction [tx *db*]
    (let [api_key (db/get-token tx {:token_type "api_key"})]
      (str (:token api_key) "@AMER.OAUTHAP"))))

(defn- needs-refresh? [token]
  (or (nil? token) (.isAfter (java.time.LocalDateTime/now) (:expiry token))))

(defn- update-access-token [tx res]
  (db/update-token!
   tx
   {:token_type "access_token"
    :token (get res "access_token")
    :expiry (.plusSeconds (java.time.LocalDateTime/now) (get res "expires_in"))}))

(defn- update-refresh-token [tx res]
  (db/update-token!
   tx
   {:token_type "refresh_token"
    :token (get res "refresh_token")
    :expiry (.plusSeconds (java.time.LocalDateTime/now) (get res "refresh_token_expires_in"))}))

(defn- post-refresh [refresh-token]
  (let [res (client/post
              (str base-url "/oauth2/token")
              {:form-params
               {:grant_type "refresh_token"
                :refresh_token refresh-token
                :access_type "offline"
                :code ""
                :client_id (get-api-key)}})
        token (parse-string (:body res))]
    token))

(defn- refresh-token [tx]
  (let [refresh-token (db/get-token tx {:token_type "refresh_token"})]
    (if (not refresh-token)
      (throw (RuntimeException. "No refresh token in database"))
      (let [res (post-refresh (:token refresh-token))]
        (update-refresh-token tx res)
        (update-access-token tx res)))))

(defn get-access-token
  ([] (jdbc/with-db-transaction [tx *db*] (get-access-token tx)))
  ([tx]
   (let [access-token (db/get-token tx {:token_type "access_token"})]
     (if (needs-refresh? access-token)
       (do
         (println "needs refresh")
         (refresh-token tx)
         (get-access-token tx))
       (:token access-token)))))

