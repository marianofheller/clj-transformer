(ns clj-writing-macros.tranformer
  (:require [clj-http.client :as client]
            [clojure.string :as s]
            [ring.util.codec :as codec]))



;; (defmacro def-attribute [attr-name body]
;;   (let [fname (symbol (str "get-attr-" (name attr-name)))
;;         tbody (-> body
;;                   (s/replace #"\:input->(\S)" "(:$1 input )")
;;                   (s/replace #"\:attr->(\S)" "(:result (get-attr-$1 input))"))]
;;     `(def ^:private ~fname
;;        (memoize (fn [~'input] {:name ~attr-name :result ~(read-string tbody)})))))





;; (do

;;   (defmacro defattr [attr-name body]
;;     (let [fname (symbol (str "get-attr-" (name attr-name)))
;;           tbody (-> body
;;                     (s/replace #"\:input->(\S)" "(:$1 input )")
;;                     (s/replace #"\:attr->(\S)" "(:result (get-attr-$1 input))"))]
;;       `(def ^:private ~fname
;;          (memoize (fn [~'input] {:name ~attr-name :result ~(read-string tbody)})))))


;;   #_{:clj-kondo/ignore [:type-mismatch]}
;;   (defattr :a (+ :input->a :input->b))


;; ;;   (defmacro def-attrs [rules-sym defs]
;; ;;     (let [bents (vec (map #(list 'quote %1) (partition 2 defs))),
;; ;;           process-entry (fn [acc [key expr]] (assoc acc key (fn [input] {:name key :result (read-string expr)})))]
;; ;;       `(def ~rules-sym (reduce ~process-entry {} ~bents))))



;;   (defmacro def-attrs [rules-sym defs]
;;     (let [bents (vec (map #(list 'quote %1) (partition 2 defs)))]
;;       `(def ~rules-sym ~(into (sorted-map) bents))))


;; ;;   (defmacro def-attrs [rules-sym defs]
;; ;;     (let [bents (vec (map vec (partition 2 defs))),
;; ;;           process-entry (fn [acc [key expr]] (assoc acc key expr))]
;; ;;       `(def ~rules-sym (reduce ~process-entry {} ~bents))))



;; ;;   (def-attrs rulesa [:a :input->a,
;; ;;                      :b (* :input->b 3)])
;;   #_{:clj-kondo/ignore [:type-mismatch]}
;;   (def-attrs rulesa [:a (identity :input->a),
;;                      :b (* :input->b 3),
;;                      :c (+ :attr->a :attr->b),
;;                      :d (* :attr->c :attr->c),
;;                      :e (/ :attr->d :input->c),
;;                      :f (+ :attr->a :attr->b :attr->c :attr->d :attr->e)])

;; ;;   (((:a rulesa) {:a 1}))

;;   rulesa)


(do
  (def transformer-a (let [r (atom {})
                           attr (fn [k input] ((k @r) input))
                           def-attr (fn [key, f] (swap! r #(assoc %1 key (memoize f))))]

                       ;; Set up transformations
                       (def-attr :a (fn [{source :source}] (:a source)))
                       (def-attr :b (fn [{source :source}] (* (:b source) 3)))
                       (def-attr :c (fn [input] (+ (attr :a input) (attr :b input))))
                       (def-attr :d (fn [input] (* (attr :c input) (attr :c input))))
                       (def-attr :e (fn [{source :source :as input}] (/  (attr :d input) (:c source))))

                       ;; Transfomer function setup
                       (fn [source] (reduce-kv (fn [m k v]  (assoc m k (v {:source source}))) {} @r))))

  (transformer-a {:a 1, :b 3, :c 2}))




(do
  (defmacro def-attr [acc attr-key def]
    (let [tdef (-> def
                   (s/replace #"\:input->(\S)" "(:$1 input )")
                   (s/replace #"\:attr->(\S)" "((:$1 @r) input)"))]
      `(swap! ~acc #(assoc %1 ~attr-key (memoize (fn [input#] ~tdef))))))

  (def transformer-b (let [r (atom {})]

                       ;; Set up transformations
                       (def-attr r :a (identity :input->a))

                       ;; Transfomer function setup
                       (fn [input] (reduce-kv (fn [m k v]  (assoc m k (v input))) {} @r))))

  (transformer-b {:a 1, :b 3, :c 2}))


(do
  (defmacro def-attrs [t-sym defs]
    (let [r (gensym 'rules),
          bents (partition 2 defs)]
      `(def ~t-sym (let [~r (atom {})]
                     (swap! ~r #(assoc %1 :a (memoize (fn [input#] (:a input#)))))
                     (map println ~bents)
                     (fn [input#] (reduce-kv (fn [m# k# v#]  (assoc m# k# (v# input#))) {} @~r))))))


  #_{:clj-kondo/ignore [:type-mismatch]}
  (def-attrs transformer-c [:a '(identity :input->a)
                            :b '(* :input->b 3),
                            :c '(+ :attr->a :attr->b),
                            :d '(* :attr->c :attr->c),
                            :e '(/ :attr->d :input->c),
                            :f '(+ :attr->a :attr->b :attr->c :attr->d :attr->e)])
  (transformer-c {:a 1, :b 3, :c 2}))

(partition 2 [1 (+ 1 2) 3 (* 1 2)])
(list 'quote `(1 2))
`(1 (+ 1 2) 3 (* 1 2))
(partition 2 `(1 (+ 1 2) 3 (* 1 2)))
(map #(%1) (partition 2 `(1 (+ 1 2) 3 (* 1 2))))

(map #(list 'quote %1) (partition 2 `(1 (+ 1 2) 3 (* 1 2))))


(vec (map vec (partition 2 [1 `(+ 1 2) 3 (* 1 2)])))

(reduce #(%2) {} (partition 2 [1 2 3 4]))

`(* 1 3)

(def a (* 1 3))

a


;; (def rulesb {
;;              :a (fn [input] (:a input))
;;              :b (fn [input] (:a input))
;;              })