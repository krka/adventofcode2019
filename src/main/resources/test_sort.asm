int i = 0
int j = 0
int tmp = 0
array[100] numbers

int outer = 0
int inner = 0

label read_input
input tmp
setarray numbers i tmp
i = i + 1
eq tmp i 100
jumpfalse tmp read_input

i = 0 + 0
label outer_loop
j = i + 1
getarray numbers i outer

label inner_loop
getarray numbers j inner
lessthan tmp inner outer
jumpfalse tmp skipswap
setarray numbers i inner
setarray numbers j outer
outer = inner + 0

label skipswap
j = j + 1

lessthan tmp j 100
jumptrue tmp inner_loop

i = i + 1
lessthan tmp i 99
jumptrue tmp outer_loop

i = 0 + 0
label emit
getarray numbers i tmp
output tmp
i = i + 1
lessthan tmp i 100
jumptrue tmp emit

halt
