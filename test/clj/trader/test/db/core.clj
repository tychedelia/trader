(ns trader.test.db.core
  (:require
   [trader.db.core :refer [*db*] :as db]
   [java-time.pre-java8]
   [luminus-migrations.core :as migrations]
   [clojure.test :refer :all]
   [clojure.java.jdbc :as jdbc]
   [trader.config :refer [env]]
   [mount.core :as mount]))

(use-fixtures
  :once
  (fn [f]
    (mount/start
     #'trader.config/env
     #'trader.db.core/*db*)
    (migrations/migrate ["migrate"] (select-keys env [:database-url]))
    (f)))

(deftest test-users
  (jdbc/with-db-transaction [t-conn *db*]
    (jdbc/db-set-rollback-only! t-conn)
    (is (= 1 (db/create-user!
              t-conn
              {:first_name "Sam"
               :last_name  "Smith"
               :email      "sam.smith@example.com"
               :pass       "pass"})))
    (is (= {:id         1
            :first_name "Sam"
            :last_name  "Smith"
            :email      "sam.smith@example.com"
            :pass       "pass"
            :admin      nil
            :last_login nil
            :is_active  nil}
           (db/get-user t-conn {:id 1})))))

(deftest test-tokens
  (jdbc/with-db-transaction [t-conn *db*]
    (jdbc/db-set-rollback-only! t-conn)
    (is (= 1 (db/update-token!
              t-conn {:token_type "refresh_token"
                      :token "abcd1234"
                      :expiry nil})))
    (is (= {:id 1
            :token_type "refresh_token"
            :token "abcd1234"
            :expiry nil}
           (db/get-token t-conn {:token_type "refresh_token"})))))
