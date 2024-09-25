(ns clj-writing-macros.domain.tranformer-test
  (:require
   [clj-writing-macros.test-utils :refer [defspec-test]]
   ;; [clojure.test :refer :all]
   [clj-writing-macros.domain.tranformer  :as t]))


(defspec-test test-transformer-a `t/transformer)


;; (binding [*print-meta* true]
;;   (macroexpand '(defspec-test test-transformer-a t/transformer-a)))

;; (def test-transformer-a (clojure.core/fn [] (clojure.test/test-var #'test-transformer-a)))
;; (defspec t/transformer-a 100)

;; (stest/check `transformer-a)


;; (stest/instrument `transformer-a)