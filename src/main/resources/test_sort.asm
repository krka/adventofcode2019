var i 0
var j
var tmp
array 100 numbers

var outer
var inner

label read_input
input tmp
setarray numbers i tmp
add i i 1
eq tmp i 100
jumpfalse tmp read_input

add i 0 0
label outer_loop
add j i 1
getarray numbers i outer

label inner_loop
getarray numbers j inner
lessthan tmp inner outer
jumpfalse tmp skipswap
setarray numbers i inner
setarray numbers j outer
add outer inner 0

label skipswap
add j j 1

lessthan tmp j 100
jumptrue tmp inner_loop

add i i 1
lessthan tmp i 99
jumptrue tmp outer_loop

add i 0 0
label emit
getarray numbers i tmp
output tmp
add i i 1
lessthan tmp i 100
jumptrue tmp emit

halt
