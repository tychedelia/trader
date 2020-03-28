(ns trader.env
  (:require
    [selmer.parser :as parser]
    [clojure.tools.logging :as log]
    [trader.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[trader started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[trader has shut down successfully]=-"))
   :middleware wrap-dev})
