(defproject mood-tracker "0.1.0-SNAPSHOT"
  :description ""
  :url ""
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.3"]
                 [metosin/reitit "0.5.15"]
                 [ring-cors "0.1.13"]
                 [http-kit "2.5.3"]
                 [org.postgresql/postgresql "42.3.1"]
                 [com.layerware/hugsql "0.5.1"]]
  :repl-options {:init-ns mood-tracker.core}
  :aliases {"runserver" ["run" "-m" "mood-tracker.core"]}
  :source-paths ["src" "src/clj"])
