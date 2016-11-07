(ns component-testing.core
  (:require [component-testing.compie :as c]
            [om.next :as om]))

(js/ReactDOM.render (c/compie {}) (js/document.getElementById "app"))

