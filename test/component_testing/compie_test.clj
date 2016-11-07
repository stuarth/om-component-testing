(ns component-testing.compie-test
  (:require [clojure.test :refer :all]
            [component-testing.compie :as c]
            [om.next :as om]
            [om.dom :as dom]
            [net.cgrand.enlive-html :as eh])
  (:import [java.io StringReader]))

(defn ->html-resource
  [html-str]
  (-> (StringReader. html-str)
      eh/html-resource))

(deftest input-validation
  (testing "no error is shown on valid input"
    (let [c (doto (c/compie {})
              (om/update-state! assoc :c "fox"))
          html-resource (-> c dom/render-to-str ->html-resource)
          c-input-group (-> html-resource
                            (eh/select [:#c-input-group])
                            first)]
      (is (nil? (get-in c-input-group [:attrs :class])))))

  (testing "error is shown on invalid input"
    (let [c (doto (c/compie {})
              (om/update-state! assoc :c "fooxq"))
          html-resource (-> c dom/render-to-str ->html-resource)
          c-input-group (-> html-resource
                            (eh/select [:#c-input-group])
                            first)]
      (is (= "error" (get-in c-input-group [:attrs :class]))))))

