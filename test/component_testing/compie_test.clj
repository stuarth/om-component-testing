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

(defmulti local-read om/dispatch)
(def local-parser (om/parser {:read local-read}))

(defmethod local-read :default
  [{:keys [state target] :as env} k params]
  (let [v (find @state k)]
    (if (or (nil? target) (some? v))
        {:value v}
        {target true})))

(defmulti remote-read om/dispatch)
(def remote-parser (om/parser {:read remote-read}))

(defmethod remote-read :default
  [env k params]
  (println "remote-read :default")
  {:value 7})

(deftest remote-read-test
  (testing "expected query is generated"
    (let [sent (promise)
          reconciler
          (om/reconciler {:state {:x 1}
                          :parser local-parser
                          :remotes [:some-remote]
                          :send (fn [{:keys [some-remote] :as ctx} cb]
                                  (deliver sent ctx))})]
      (om/add-root! reconciler c/Compie nil)
      (is (=  {:some-remote [:y]} @sent)))))



