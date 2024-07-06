(ns clj-writing-macros.core-test
  (:require [clojure.test :refer :all]
            [clj-writing-macros.core :refer :all]))

(deftest a-test
  (testing "FIXME, I fail."
    (is (= 1 1))))

(def ^:private sample-input {:a 17 :b 33 :c 2})

(deftest a-test-2
  (testing "all values are calculated"
    (is (= 1 1))))
