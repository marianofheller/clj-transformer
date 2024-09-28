(ns build
  (:require [clojure.tools.build.api :as b]))


(def app 'app-ns/appname)

(def version (format "0.1.%s" (b/git-count-revs nil)))
(def class-dir "target/classes")
(def uber-file (format "target/%s-%s-standalone.jar" (name app) version))

;; delay to defer side effects (artifact downloads)
(def basis (delay (b/create-basis {:project "deps.edn"})))

(defn clean [_]
  (b/delete {:path "target"}))

(defn uber [_]
  (clean nil)

  (b/copy-dir {:src-dirs ["src" "resources"]
               :target-dir class-dir})

  (b/compile-clj {:basis @basis
                  :class-dir class-dir
                  :ns-compile '[clj-writing-macros.core]})

  (b/uber {:basis @basis
           :class-dir class-dir
           :main 'clj-writing-macros.core
           :uber-file uber-file}))