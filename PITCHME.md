### Autonomous Driving Android Car 
developed by [B. Grau](https://github.com/SuperCrazyKing) and [D. Lagamtzis](https://github.com/umadbro96) (since 2018)

<img src="assets/gitpitch/Cropped%20Car%20(small).png" width="450" height="290">
---

# Agenda

* Intro
* Stand der Technik
* Implementierung
* Prototyp
* Beispiele
* Fazit

---

# Intro 

Android App die es ermöglicht durch Bilderkennungsalgorithmen ein Modellauto durch eine Teststrecke selbstständig fahren zu lassen.

Dieses Projekt wurde gewählt, um herauszufinden, ob es möglich ist so eine Aufgabe mit einem Smartphone zu lösen. Außerdem wollten wir das OpenCV Framework kennenlernen. 

---

# Stand der Technik

* OpenCV 
* Kommunikationslösung - Bluetooth / USB OTG
* Arduino als Blackbox

<img src="assets/gitpitch/architecture%20(small).png" width="500" height="300">
---

# Implementierung
* Pipeline
   - rgb2hsv
   - Binarisierung (bitwise_and) weißwert-skalierung
   - mat2gray - Graustufenbild
   - Canny Image (Adjazenzpixelvergleich)
   - Hough Linien -> Kantenpunkte zu einer Linie zusammenfassen
   - `Retransformation` zum Ursprungs RGB-Bild mit erkanntem Pattern

+++

* Entscheidungsprozess
   - Durchschnittswinkel als gefundenen Winkel berechen -> Lenkrichtung bestimmen
---

# Prototyp

### Live Demo !
<img src="https://www.neuronsw.com/wp-content/uploads/2017/09/livedemo-1.png" width="500" height="300">
###### Source : neuronsw.com
---

# Beispiele
   - [>>Hier<<](https://github.com/umadbro96/androidAICar/tree/master/aiopencv/app/src/main/java/stud/edu/aiopencv) kann direkt in den Code gesprungen werden
   
   - [>>Hier<<](https://github.com/umadbro96/androidAICar/tree/master/assets) lassen sich die aufgenommenen Medien finden 
   

---
# Fazit

* [X] Mustererkennung auf Bildern
* [  ] Straßenschilder (statische Hindernisse)
* [  ] Menschen (dynamische Hinternisse)

+++

* Smartphone -> Machine Vision
* Smartphone -> NeuralNetwork KI via Import von trainierten Modellen
* nVidia Jetson Board / Hightech CameraGPU Setup -> End-to-End KI

+++

* Reinforcement Learning für dynamische Hindernisse, wenn Interaktion mit gesamter Umgebung notwendig
* siehe [Athena](https://www.daimler.com/innovation/case/autonomous/bosch-kooperation.html) Projekt
---

