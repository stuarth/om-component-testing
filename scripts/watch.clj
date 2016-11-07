(require '[cljs.build.api :as b])

(b/watch "src"
  {:main 'component-testing.core
   :output-to "out/component_testing.js"
   :output-dir "out"})
