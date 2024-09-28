(ns clj-writing-macros.core
  (:gen-class)
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [ring.logger :as logger]
            [clojure.tools.logging :as log]
            [clj-writing-macros.api.http :refer [app]]))

(defn ring-log-fn [{level :level message :message}]
  (log/log level nil message))

(defn -main
  [& _args]
  (run-jetty
   (logger/wrap-with-logger app {:log-fn ring-log-fn})
   {:port 3000}))
