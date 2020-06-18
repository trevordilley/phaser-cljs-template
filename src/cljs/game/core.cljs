(ns ^:dev/once game.core
  (:require
            ["phaser" :as phaser]
            [game.config :as config]
            ))


(def sum_dt (atom 0))
(def dilation (atom 1))
(def sum-dt-text (atom (str @sum_dt)))
(def dt-text (atom nil))
(def dilation-text (atom (str @dilation)))

(defn ph-preload [scene]
  (println "preloading")
  )

(defn ph-create [scene]
  (reset! sum-dt-text (-> scene .-add (.text 50 50)))
  (reset! dilation-text (-> scene .-add (.text 300 50)))
  (reset! dt-text (-> scene .-add (.text 500 50)))
  )
(defn ph-update [scene timestep dt]
  (let [y (.. scene
              -sys
              -config
              -gameTitle
              )
        ]
    (swap! sum_dt #(+ @sum_dt dt))
    (.setText @sum-dt-text (str "Sum: " @sum_dt))
    (.setText @dilation-text (str "Dilation: " @dilation))
    (.setText @dt-text (str "DT: " (* dt @dilation)))
    )

  )

(def config (atom {
                   :width  800
                   :height 500
                   :parent "app"
                   :type   (.-AUTO phaser)
                   :physics {
                             :default "arcade"
                             :arcade {
                                      :debug false
                                      }

                             }
                   :scene  {
                            :gameTitle "My Game"
                            :preload   (fn [] (this-as scene (ph-preload scene)))
                            :create    (fn [] (this-as scene (ph-create scene)))
                            :update    (fn [timestep, dt] (this-as scene (ph-update scene timestep dt )))
                            }
                   }))


(defonce game (atom nil))

(defn ^:dev/after-load mount-root []
   (if-not @game
     (do
       (println "Creating Phaser Game")
       (reset! game (js/Phaser.Game. (clj->js @config))))
     )
  )

(defn dev-setup []
  (when config/debug?
    (println "dev mode")))

(defn init []
  (dev-setup)
  (mount-root)
  )


