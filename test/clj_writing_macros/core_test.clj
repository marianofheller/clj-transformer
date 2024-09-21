(ns clj-writing-macros.core-test)
  ;; (:require [clojure.test :refer :all]
  ;;           [clojure.test.check :as tc]
  ;;           [clojure.test.check.generators :as gen]
  ;;           [clojure.test.check.properties :as prop]
  ;;           [clj-writing-macros.core :refer :all]))


;; (deftest test-sample-input-output
;;   (let [input {:a 17 :b 33 :c 2}
;;         output (get-output input)
;;         expected {:e 6728 :c 116 :a 17 :f 20416 :d 13456 :b 99}]
;;     (testing "all values are calculated"
;;       (is (= output expected)))))



;; (def inputs-gen (gen/hash-map
;;                  :a (gen/double* {:min 0, :max 99999, :infinite? false, :NaN? false}),
;;                  :b (gen/double* {:min 0, :max 99999, :infinite? false, :NaN? false}),
;;                  :c (gen/double* {:min 1, :max 99999, :infinite? false, :NaN? false})))


;; (def check-gen-inputs
;;   (prop/for-all [input inputs-gen]
;;                 (let [output (get-output input)]
;;                   (every? #(not (nil? %1)) (vals output)))))

;; (deftest fuzzy-test
;;   (testing "all values are calculated"
;;     (let [test-output (tc/quick-check 100 check-gen-inputs)]
;;       (is (= (:pass? test-output) true)))))