# clj-writing-macros

### TODO

- Observability
  - https://logback.qos.ch/manual/introduction.html
- remote repl
- Show formulas in UI
- DB?
- auth
- Explore error handling more?

### Useful commands

- clojure -M:run/service
- clojure -M:otel:run/service
- clojure -X:test/run

### Other stuff

```clojure

(defn- download-file [url out-dir]
  (let [my-file (last (.split url "/"))
        out-path (str out-dir "/" my-file)]
    (if-not (.exists  (io/as-file out-path))
      (do
        (.mkdirs (java.io.File. out-dir))
        (future (with-open [in (io/input-stream url)
                            out (io/output-stream out-path)]
                  (io/copy in out)
                  (println  my-file " has been downloaded."))))
      (print my-file "is already there"))))
```
