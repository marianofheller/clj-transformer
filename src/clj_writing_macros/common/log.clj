(ns clj-writing-macros.common.log
  (:require [clojure.pprint :as pprint])
  #_{:clj-kondo/ignore [:unused-import]}
  (:import [ch.qos.logback.classic Level Logger]
           [java.io StringWriter]
           [org.slf4j LoggerFactory MDC]))

(def logger ^ch.qos.logback.classic.Logger (LoggerFactory/getLogger "clj-writing-macros"))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn set-log-level!
  "Pass keyword :error :info :debug"
  [level]
  (case level
    :debug (.setLevel logger Level/DEBUG)
    :info (.setLevel logger Level/INFO)
    :error (.setLevel logger Level/ERROR)))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defmacro with-logging-context [context & body]
  "Use this to add a map to any logging wrapped in the macro. Macro can be nested.
  (with-logging-context {:key \"value\"} (log/info \"yay\"))
  "
  `(let [wrapped-context# ~context
         ctx# (MDC/getCopyOfContextMap)]
     (try
       (if (map? wrapped-context#)
         (doall (map (fn [[k# v#]] (MDC/put (name k#) (str v#))) wrapped-context#)))
       ~@body
       (finally
         (if ctx#
           (MDC/setContextMap ctx#)
           (MDC/clear))))))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defmacro debug [& msg]
  `(.debug logger (print-str ~@msg)))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defmacro info [& msg]
  `(.info logger (print-str ~@msg)))

(defmacro error [throwable & msg]
  `(if (instance? Throwable ~throwable)
     (.error logger (print-str ~@msg) ~throwable)
     (.error logger (print-str ~throwable ~@msg))))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defmacro spy
  [expr]
  `(let [a# ~expr
         w# (StringWriter.)]
     (pprint/pprint '~expr w#)
     (.append w# " => ")
     (pprint/pprint a# w#)
     (error (.toString w#))
     a#))