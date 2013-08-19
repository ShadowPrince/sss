(ns sss.graphics.canvas
  "Canvas - bitmap designed for painting on it (formally ordinary bitmap)"
  (:require [sss.graphics.bitmap :refer [char-at]]))

(defn canvas 
  "Create canvas with ~w(idth) and ~h(eight)"
  [w h]
  (repeat h (repeat w " ")))

(defn w 
  "Get width of ~canvas"
  [canvas]
  (if (empty? canvas)
    0
    (count (first canvas))))

(defn h 
  "Get height of ~canvas"
  [canvas]
  (count canvas))

(defn bitmap-paint-cords 
  "Calculate coordinates depending of ~y ~x ~t(op) ~l(eft)"
  [y x t l]
  [(- x l) (- y t)])

(defn paint-padding 
  "Paint ~bitmap into ~canvas with padding of ~t(op) and ~l(eft)"
  [canvas bitmap t l]
  (map-indexed 
    (fn [y row]
      (map-indexed
        (fn [x px]
          (let [bpx (apply (partial char-at bitmap) (bitmap-paint-cords y x t l))]
            (if (nil? bpx)
              px
              bpx)))
        row))
    canvas))

(defn paint 
  "Paint ~bitmap into ~canvas, with padding defined with :t :l or :b :r."
  ([canvas bitmap & {:keys [t l b r] :or [] :as padding}]
   (let [l (if (nil? l) (- r (count (first bitmap))) l)
         t (if (nil? t) (- b (count bitmap)) t)]
     (paint-padding canvas bitmap t l))))

(defmacro in-paint 
  "Macro will insert 'paint ~canvas' in each of ~forms"
  [canvas & forms]
  (let [forms# (map #(concat (list paint) %) forms)]
    `(-> ~canvas ~@forms#)))

(defn crop 
  "Crop ~canvas with ~w(idth), ~h(eight), ~t ~l or ~b ~r"
  [canvas w h t l b r]
  (let [ta (if (pos? t) t 0)
        la (if (pos? l) l 0)

        t (if (nil? t) 0 t)
        l (if (nil? l) 0 l)
        t (if (pos? t) 0 t)
        l (if (pos? l) 0 l)


        h (+ (if (nil? h) (count canvas) (- h t)) ta)
        w (+ (if (nil? w) (count (first canvas)) (- w l)) la)
        
        canvas (if (pos? ta) (concat (repeat ta " ") canvas) canvas)
        canvas (if (pos? la) (map #(concat (repeat la " ") %) canvas) canvas)]
  (map #(take w (drop (-> l Math/abs int) %)) 
       (take h (drop (-> t Math/abs int) canvas)))))

