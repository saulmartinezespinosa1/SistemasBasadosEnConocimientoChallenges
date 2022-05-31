(defrule promocion1
   (producto (nombre Note22)(id ?p))
   =>
   (assert (promocion (id ?p)(descripcion "Funda de regalo")))
)


(defrule meses_si1
   (producto (nombre iPhone13) (id ?p))
   (tarjeta (banco Banamex))
   =>
   (assert (mesessi (id ?p) (banco Banamex) (msi 24)))
)

(defrule meses_si2
   (producto (nombre Note12) (id ?p))
   (tarjeta (banco Liverpool))
   =>
   (assert (mesessi (id ?p) (banco Liverpool) (msi 12)))
)


(defrule meses_si1_1
   (producto (nombre iPhone13))
   (tarjeta (banco Banamex))
   =>
   (printout t "en la compra de 1 Apple iPhone13 y pago con Banamex tienes 24 MSI" crlf))


(defrule meses_si2_1
   (producto (nombre Note12))
   (tarjeta (banco Liverpool))
   =>
   (printout t "en la compra de 1 Samsung Note12 y pago con Liverpool tienes 12 MSI" crlf))
