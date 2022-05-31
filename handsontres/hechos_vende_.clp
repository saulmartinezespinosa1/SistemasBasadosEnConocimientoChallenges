(deftemplate producto
  (slot categoria)
  (slot nombre)
  (slot id)
  (slot marca)
  (slot precio)
  (slot existencia))

(deftemplate tarjeta
   (slot banco) (slot id))

(deftemplate mesessi
 (slot id)
 (slot banco)
 (slot msi))

(deftemplate promocion
 (slot id)
 (slot descripcion))
 
(deffacts productos
(producto (categoria telefono)(nombre iPhone7)(id 1) (marca apple) (precio 6000) (existencia 35))
(producto (categoria telefono)(nombre iPhone11)(id 2) (marca apple) (precio 25000) (existencia 45))
(producto (categoria telefono)(nombre iPhone13)(id 3) (marca apple) (precio 25000) (existencia 45))
(producto (categoria telefono)(nombre S21)(id 4) (marca Samsung) (precio 12000) (existencia 55))
(producto (categoria telefono)(nombre Note12)(id 5) (marca Samsung) (precio 12000) (existencia 55))
(producto (categoria telefono)(nombre MatePro)(id 6) (marca Huawei) (precio 29000) (existencia 25))
(producto (categoria telefono)(nombre P50)(id 7) (marca Huawei) (precio 24000) (existencia 25))
(producto (categoria laptop)(nombre MacBook)(id 8) (marca apple) (precio 35000) (existencia 65))
(producto (categoria laptop)(nombre ThinkPad_T480)(id 9) (marca apple) (precio 23000) (existencia 75))
(producto (categoria laptop)(nombre Vivobook_15)(id 10) (marca apple) (precio 13000) (existencia 40))
(producto (categoria laptop)(nombre Chromebook_A4)(id 11) (marca apple) (precio 3500) (existencia 20))
(producto (categoria telefono)(nombre Note22)(id 12) (marca Samsung) (precio 12000) (existencia 55))
)

(deffacts tarjetas
(tarjeta (banco Afirme)(id 1))
(tarjeta (banco Actinver)(id 2))
(tarjeta (banco AmericanExpress)(id 3))
(tarjeta (banco Azteca)(id 4))
(tarjeta (banco BBVA)(id 5))
(tarjeta (banco Banorte)(id 6))
(tarjeta (banco Banamex)(id 7))
(tarjeta (banco HSBC)(id 8))
(tarjeta (banco Liverpool)(id 9))
(tarjeta (banco Santander)(id 10))
)
