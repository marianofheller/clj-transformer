(ns clj-writing-macros.api.http
  (:require [hiccup2.core :as h]
            [ring.middleware.multipart-params :refer (wrap-multipart-params)]
            [ring.middleware.multipart-params.byte-array :refer (byte-array-store)]
            [clj-writing-macros.domain.sheet-parser :refer (parse-xslx)]
            [clj-writing-macros.domain.transformer :refer (transformer)]
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

(defn- handle-success [transformed-rows]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (str (h/html
               (h/raw "<!DOCTYPE html>")
               [:html {:lang "en"}
                [:body
                 [:div
                  (map (fn [r] [:p (str r)]) transformed-rows)]]]))})

(defn- handle-error [error]
  {:status 400
   :headers {"Content-Type" "text/html"}
   :body (str (h/html
               (h/raw "<!DOCTYPE html>")
               [:html {:lang "en"}
                [:body
                 [:div (ex-message error)]]]))})

(defn transform-output-page [request]
  (try
    (->> request
         ((comp :bytes :finput :params))
         (parse-xslx)
         (map transformer)
         (handle-success))
    (catch Exception error
      (handle-error error))))

(c/defroutes main-routes
  (c/GET "/" [] (transform-input-page))
  (c/POST "/transform" req (transform-output-page req))
  (c/GET "/transform" [] {:status 301 :headers {"Location" "/"}})
  (route/resources "/")
  (route/not-found "Page not found"))

(def app
  (-> (handler/site main-routes)
      (wrap-multipart-params {:store (byte-array-store)})))
