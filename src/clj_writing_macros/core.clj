(ns clj-writing-macros.core
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [clj-writing-macros.api :refer [app]]))



(defn -main
  [& _args]
  (run-jetty app {:port 3000}))

  ;; (:require [clj-http.client :as client]
  ;;           [clojure.string :as s]
  ;;           [ring.util.codec :as codec]))

;; (defn foo
;;   "I don't do a whole lot."
;;   [x]
;;   (println x "Hello, World!!!!"))

;; (defmacro code-critic
;;   "Phrases are courtesy Hermes Conrad from Futurama"
;;   [bad good]
;;   (list 'do
;;         (list 'println
;;               "Great squid of Madrid, this is bad code:"
;;               (list 'quote bad))
;;         (list 'println
;;               "Sweet gorilla of Manila, this is good code:"
;;               (list 'quote good))))

;; (defmacro code-critic-2
;;   "some docs"
;;   [bad good]
;;   `(do
;;      (println "Great squid of Madrid, this is bad code:" (quote ~bad))
;;      (println  "Sweet gorilla of Manila, this is good code:" (quote ~good))))

;; (defn criticize-code
;;   [critisism code]
;;   `(println ~critisism (quote ~code)))

;; (defmacro code-critic-3
;;   "some-docs"
;;   [bad good]
;;   `(do
;;      ~(criticize-code "Great squid of Madrid, this is bad code:" bad)
;;      ~(criticize-code "Sweet gorilla of Manila, this is good code:" good)))

;; (defmacro code-critic-4
;;   "some-docs"
;;   [bad good]
;;   `(do
;;      ~@(map #(apply criticize-code %1) [["Great squid of Madrid, this is bad code:" bad]
;;                                         ["Sweet gorilla of Manila, this is good code:" good]])))

;; (defmacro wait
;;   "wait and fire"
;;   [timeout-ms & block]
;;   `(do (Thread/sleep ~timeout-ms) ~@block))

;; (defmacro fire-with-delay
;;   "wait and fire"
;;   [timeout-ms & block]
;;   `(future (Thread/sleep ~timeout-ms) ~@block))

;; (defmacro enqueue
;;   ([queue holder promise-gen receiver]
;;    `(let [~holder (promise)]
;;       (future (deliver ~holder ~promise-gen))
;;       (deref ~queue)
;;       ~receiver
;;       ~holder))
;;   ([holder promise-gen receiver]
;;    `(enqueue (future) ~holder ~promise-gen ~receiver)))

;; ;(-> (enqueue ➊saying ➋(wait 200 "'Ello, gov'na!") ➌(println @saying))
;; ;   ➍(enqueue saying (wait 400 "Pip pip!") (println @saying0))
;; ;    (enqueue saying (wait 100 "Cheerio!") (println @saying0)))

;; (defn search-youtube
;;   [input]
;;   (let [url  (str "https://www.google.com/search?btnI=1&q=" input)
;;         response (client/get url)]
;;     (last (:trace-redirects response))))

;; (defn search-duckduckgo
;;   [input]
;;   (let [url  (str "https://duckduckgo.com/?q=!ducky+" input)
;;         response (client/get url)
;;         extract-url (fn extract-url [body] (codec/url-decode (last (re-find #"replace\('\/l\/\?uddg=(.*)'\)" body))))]
;;     (extract-url (:body response))))

;; (defn search
;;   [input]
;;   (let [result-google (future (search-youtube input))
;;         result-duckduckgo (future (search-duckduckgo input))]
;;     {:youtube @result-google
;;      :duckduckgo @result-duckduckgo}))

;; (defn mmemoize [f]
;;   (let [mem (atom {})]
;;     (fn [& args]
;;       (if-let [e (find @mem args)]
;;         (val e)
;;         (let [ret (apply f args)]
;;           (swap! mem assoc args ret)
;;           ret)))))

;; (defn fib [n]
;;   (if (<= n 1)
;;     n
;;     (+ (fib (dec n)) (fib (- n 2)))))

;; (time (fib 35))

;; (def fib-2 (mmemoize fib))

;; (time (let [a (fib-2 35) b (fib-2 35)] b))

;; ;; --------------------------------
;; ;; a = input.a
;; ;; b = input.b * 3
;; ;; c = a + b
;; ;; d = c * c
;; ;; e = d / input.c
;; ;; f = a + b + c + d + e

;; (defmacro def-attribute [attr-name body]
;;   (let [fname (symbol (str "get-attr-" (name attr-name)))
;;         tbody (-> body
;;                   (s/replace #"\:input->(\S)" "(:$1 input )")
;;                   (s/replace #"\:attr->(\S)" "(:result (get-attr-$1 input))"))]
;;     `(def ^:private ~fname
;;        (memoize (fn [~'input] {:name ~attr-name :result ~(read-string tbody)})))))

;; ;; (def sample-input {:a 17 :b 33 :c 2})

;; (def-attribute :a :input->a)
;; (def-attribute :b (* :input->b 3))
;; (def-attribute :c (+ :attr->a :attr->b))
;; (def-attribute :d (* :attr->c :attr->c))
;; (def-attribute :e (/ :attr->d :input->c))
;; (def-attribute :f (+ :attr->a :attr->b :attr->c :attr->d :attr->e))

;; ;; (get-attr-a sample-input)
;; ;; (get-attr-b sample-input)
;; ;; (get-attr-c sample-input)
;; ;; (get-attr-d sample-input)
;; ;; (get-attr-e sample-input)

;; (def ^:private getters
;;   (let [this-ns (symbol (namespace ::x))
;;         all-fns (keys (ns-publics this-ns))
;;         getter-names (filter #(s/starts-with? %1 "get-attr-") all-fns)]
;;     (map #(ns-resolve this-ns (symbol %1)) getter-names)))

;; (defn get-output
;;   [input]
;;   (let [results (map #(%1 input) getters)]
;;     (reduce #(assoc %1 (:name %2) (:result %2)) {} results)))

;; (defn add [a b] (+ a b))
;; ;; (get-output sample-input)
