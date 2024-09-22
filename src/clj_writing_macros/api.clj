(ns clj-writing-macros.api
  (:require [hiccup2.core :as h]
            [ring.middleware.multipart-params :refer (wrap-multipart-params)]
            [ring.middleware.multipart-params.byte-array :refer (byte-array-store)]
            [clj-writing-macros.sheet-parser :refer (parse-xslx)]
            [clj-writing-macros.tranformer :refer (transformer-a)]
            [compojure.core :as c]
            [compojure.route :as route]
            [compojure.handler :as handler]))

(defn transform-input-page []
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (str (h/html
               (h/raw "<!DOCTYPE html>")
               [:html {:lang "en"}
                [:body
                 [:form {:method "POST" :action "/transform" :enctype "multipart/form-data"}
                  [:div
                   [:label {:for "finput"}]
                   [:input {:type "file" :id "finput" :name "finput" :required true}]]
                  [:div
                   [:input {:type "submit" :value "Submit"}]]]]]))})


(defn transform-output-page [request]
  (let [finput (:finput (:params request))
        parsed-rows (parse-xslx (:bytes finput))
        transformed-rows (map transformer-a parsed-rows)]
    {:status 200
     :headers {"Content-Type" "text/html"}
     :body (str (h/html
                 (h/raw "<!DOCTYPE html>")
                 [:html {:lang "en"}
                  [:body
                   [:div
                    (map (fn [r] [:p (str r)]) transformed-rows)]]]))}))

(c/defroutes main-routes
  (c/GET "/" [] (transform-input-page))
  (c/POST "/transform" req (transform-output-page req))
  (route/resources "/")
  (route/not-found "Page not found"))



(def app
  (-> (handler/site main-routes)
      (wrap-multipart-params {:store (byte-array-store)})))
