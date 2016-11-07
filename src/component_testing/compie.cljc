(ns component-testing.compie
  (:require #?@(:cljs [[om.next :as om :refer-macros [defui]]
                       [sablono.core :refer-macros [html]]]
                :clj  [[om.next :as om :refer        [defui]]
                       [om-html.html :refer [html]]])
            [om.dom :as dom]
            [clojure.string :as str]))

(defn valid-input?
  [s]
  (str/includes? s "fox"))

(defui Compie
  Object
  (initLocalState [this] {:c "foo"})

  (render
   [this]
   (let [{:keys [c]} (om/get-state this)]
     (html
      [:#c-input-group
       (cond-> {:key "c-input-group"}
         (not (valid-input? c))
         (assoc :class "error"))
       [:input {:type "text"
                :value c
                :on-change (fn [ev]
                             (let [new-value (.. ev -target -value)]
                               (om/update-state! this assoc :c new-value)))}]]))))

(def compie (om/factory Compie))
