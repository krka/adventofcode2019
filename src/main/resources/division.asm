int i = 0
int k = 1
int cmp = 0
int kd = 0
int kdneg = 0
int q = 0
array[64] powtwo

label setup_powtwo
setarray powtwo i k
k = k * 2
i = i + 1
eq cmp i 64
jumpfalse cmp setup_powtwo

func div N D

i = 0
kd = D
label find_range
i = i + 1
kd = kd * 2
lessthan cmp N kd
jumpfalse cmp find_range


q = 0
label loop_start
i = i + -1
getarray powtwo i k
kd = k * D
lessthan cmp N kd
jumptrue cmp next_iter
q = q + k
kdneg = kd * -1
N = N + kdneg
label next_iter
jumptrue i loop_start
return q N
endfunc
