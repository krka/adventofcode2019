var s
var i
var ch

func outputString sptr
add s sptr 0
add i 0 0
label loop
getarrayptr s i ch
jumpfalse ch finish
output ch
add i i 1
jumpfalse 0 loop
label finish
return
endfunc
