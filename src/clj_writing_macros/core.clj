(ns clj-writing-macros.core
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [ring.logger :as logger]
            [clj-writing-macros.common.log :as log]
            [clj-writing-macros.api.http :refer [app]]))

(defn -main
  [& _args]
  (run-jetty
   (logger/wrap-with-logger app {:log-fn #(.info log/logger (print-str %))})
   {:port 3000}))
