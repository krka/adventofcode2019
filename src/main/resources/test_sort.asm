int i = 0
int j = 0
int tmp = 0
array[100] numbers

int outer = 0
int inner = 0

read_input:
tmp = input()
numbers[i] = tmp
i = i + 1
if i != 100 jump read_input

i = 0
outer_loop:
j = i + 1
outer = numbers[i]

inner_loop:
inner = numbers[j]
if inner >= outer jump skipswap
numbers[i] = inner
numbers[j] = outer
outer = inner

skipswap:
j = j + 1

if j < 100 jump inner_loop

i = i + 1
if i < 99 jump outer_loop

i = 0
emit:
tmp = numbers[i]
output(tmp)
i = i + 1
if i < 100 jump emit

halt()
