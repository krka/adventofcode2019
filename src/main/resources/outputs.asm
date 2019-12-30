include outputstring.asm
include division.asm

array 20 tempnumber
var i
var tempn
var remainder

func outputNumber n
add i 0 0
add tempn n 0

label populate
call div tempn 10 : tempn remainder
add remainder remainder 48
setarray tempnumber i remainder
add i i 1
jumptrue tempn populate

label output
add i i -1

getarray tempnumber i tempn
output tempn
jumpfalse i finish
jumpfalse 0 output

label finish
return

endfunc

