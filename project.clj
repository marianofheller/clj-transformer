(defproject clj-writing-macros "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :main clj-writing-macros.core
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [ring/ring-core "1.12.1"]
                 [ring/ring-jetty-adapter "1.12.1"]
                 [ring/ring-codec "1.2.0"]
                 [compojure "1.7.1"]
                 [hiccup "2.0.0-RC3"]
                 [commons-io/commons-io "2.17.0"]
                 [org.apache.poi/poi "5.3.0"]
                 [org.apache.poi/poi-ooxml "5.3.0"]
                 [org.clojure/test.check "1.1.1"]]
  :profiles {:test {:injections [(require 'clojure.pprint)]
                    :test-report {:reporters [clojure.pprint/pprint]}}}

  :repl-options {:init-ns clj-writing-macros.core})
