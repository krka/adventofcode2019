var tmp

func swap a b
return b a
endfunc

var x
var y
var z
var w

add x 100 0
add y 23 0

call swap x y : z w
output z
output w

halt
