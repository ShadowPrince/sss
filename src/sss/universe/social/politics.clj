(ns sss.universe.social.politics
  (:require [sss.universe.planet.core :as planet]))


(defn choose-capitals [universe union]
  (let [capitals (map 
                   #(-> (get (-> universe :social :races) %) :homeland) 
                   (:races union))]
    capitals
  ))

(defn process-universe [universe]
  (let [capitals (map choose-capitals (repeat universe) (-> universe :social :unions))]
    (prn capitals)
    (update-in 
      universe 
      [:space]
      #(-> %
           ;(setup-capitals capitals)
           ))))