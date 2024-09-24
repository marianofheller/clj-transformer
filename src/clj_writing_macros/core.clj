(ns clj-writing-macros.core
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [clj-writing-macros.api.http :refer [app]]))

(defn -main
  [& _args]
  (run-jetty app {:port 3000}))
