var i
var k
var cmp
var kd
var kdneg
var q
array 64 powtwo

add i 0 0
add k 1 0

label setup_powtwo
setarray powtwo i k
mul k k 2
add i i 1
eq cmp i 64
jumpfalse cmp setup_powtwo

func div N D

add i 0 0
add kd D 0
label find_range
add i i 1
mul kd kd 2
lessthan cmp N kd
jumpfalse cmp find_range


add q 0 0
label loop_start
add i i -1
getarray powtwo i k
mul kd k D
lessthan cmp N kd
jumptrue cmp next_iter
add q q k
mul kdneg kd -1
add N N kdneg
label next_iter
jumptrue i loop_start
return q N
endfunc
