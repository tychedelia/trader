(ns trader.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[trader started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[trader has shut down successfully]=-"))
   :middleware identity})
