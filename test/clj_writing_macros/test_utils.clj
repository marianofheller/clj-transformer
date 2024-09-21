(ns clj-writing-macros.test-utils (:require [clojure.test :as t]
                                            [clojure.spec.test.alpha :as stest]
                                            [clojure.string :as str]))




(defn explain-failed-check
  "Custom explain method to override default clojure.spec.alpha/explain-printer."
  [fc]
  (if fc
    (let [qc-result (:clojure.spec.test.check/ret fc)]
      (pr (select-keys qc-result [:fail :seed]))
      (newline)
      (pr (dissoc (stest/abbrev-result fc)  :failure))
      (newline))
    (println "Success!? Should not print this.")))

(defmacro defspec-test
  ([name sym-or-syms] `(defspec-test ~name ~sym-or-syms nil))
  ([name sym-or-syms opts]
   (when t/*load-tests*
     `(def ~(vary-meta name assoc
                       :test `(fn []
                                (let [check-results# (stest/check ~sym-or-syms ~opts)
                                      checks-passed?# (every? nil? (map :failure check-results#))]
                                  (if checks-passed?#
                                    (t/do-report {:type    :pass
                                                  :message (str "Generative tests pass for "
                                                                (str/join ", " (map :sym check-results#)))})
                                    (doseq [failed-check# (filter :failure check-results#)
                                            :let [r# (stest/abbrev-result failed-check#)
                                                  qc-result# (:clojure.spec.test.check/ret failed-check#)
                                                  failure# (:failure failed-check#)]]
                                      (t/do-report {:type     :fail
                                                    :message  (with-out-str (explain-failed-check failed-check#))
                                                    :expected (->> r# :spec rest (apply hash-map) :ret)
                                                    :actual   (if (instance? Throwable failure#)
                                                                failure#
                                                                (:stest/val failure#))})))
                                  checks-passed?#)))
        (fn [] (t/test-var (var ~name)))))))