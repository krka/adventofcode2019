int i = 0
int j = 0
int tmp = 0
array[100] numbers

int outer = 0
int inner = 0

read_input:
tmp = input()
setarray numbers i tmp
i = i + 1
tmp = i == 100
if not tmp jump read_input

i = 0
outer_loop:
j = i + 1
getarray numbers i outer

inner_loop:
getarray numbers j inner
tmp = inner < outer
if not tmp jump skipswap
setarray numbers i inner
setarray numbers j outer
outer = inner + 0

skipswap:
j = j + 1

tmp = j < 100
if tmp jump inner_loop

i = i + 1
tmp = i < 99
if tmp jump outer_loop

i = 0 + 0
emit:
getarray numbers i tmp
output(tmp)
i = i + 1
tmp = i < 100
if tmp jump emit

halt
