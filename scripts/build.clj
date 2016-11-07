(require '[cljs.build.api :as b])

(println "Building ...")

(let [start (System/nanoTime)]
  (b/build "src"
    {:main 'component-testing.core
     :output-to "resources/public/js/component_testing.js"
     :output-dir "resources/public/js"
     :asset-path "js"
     :verbose true})
  (println "... done. Elapsed" (/ (- (System/nanoTime) start) 1e9) "seconds"))


