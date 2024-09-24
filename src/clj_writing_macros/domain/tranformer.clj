(ns clj-writing-macros.domain.tranformer
  (:require
   [clojure.spec.alpha :as s]))


(let [r (atom (array-map))
      attr (fn [k input] ((k @r) input))
      def-attr (fn [key, f] (swap! r #(assoc %1 key (memoize f))))]

  ;; Set up transformations
  (def-attr :a (fn [{source :source}] (:a source)))
  (def-attr :b (fn [{source :source}] (*' 3 (:b source))))
  (def-attr :c (fn [input] (+' (attr :a input) (attr :b input))))
  (def-attr :d (fn [input] (*' (attr :c input) (attr :c input))))
  (def-attr :e (fn [{source :source :as input}] (/  (attr :d input) (:c source))))


   ;; Set up input constraints
  (s/def :source/a number?)
  (s/def :source/b number?)
  (s/def :source/c (s/and number? #(> % 0)))

  (s/def :input/source (s/keys :req-un [:source/a,
                                        :source/b,
                                        :source/c]))

  ;; Transfomer function setup
  (s/fdef transformer-a
    :args (s/and (s/cat :source :input/source)))

  (defn- _transformer [source]
    (try
      (reduce-kv (fn [m k v]  (assoc m k (v {:source source}))) {} @r)
      (catch Exception _e
        (throw
         (ex-info "There was an error transforming." {:type :error-tranforming})))))

  (defn transformer [source]
    (if (s/valid?  :input/source source)
      (_transformer source)
      (throw
       (ex-info "Invalid input. Cannot transform." {:type :invalid-input})))))


;; {:pre [(s/valid? :input/source source)]}


;; (transformer-a {:a 1, :b 3, :c 3})