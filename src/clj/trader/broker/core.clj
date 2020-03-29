(ns trader.broker.core
  (:require
   [trader.broker.auth :as auth]
   [clj-http.client :as client]
   [cheshire.core :refer [generate-string parse-string]])
  )

(def base-url "https://api.tdameritrade.com/v1")

(defn- default-headers []
  {:authorization (str "Bearer " (auth/get-access-token))})

(defn- default-body []
  {:as :json
   :headers (default-headers)})

(defn account [id]
  (:body (client/get (str base-url "/accounts/" id) (default-body))))

(defn accounts []
  (:body (client/get (str base-url "/accounts") (default-body))))
