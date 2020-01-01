int i = 0
int k = 1
int kd = 0
int kdneg = 0
int q = 0
array[64] powtwo

setup_powtwo:
setarray powtwo i k
k = k * 2
i = i + 1
if i != 64 jump setup_powtwo

func div N D

i = 0
kd = D
find_range:
i = i + 1
kd = kd * 2
if N >= kd jump find_range


q = 0
loop_start:
i = i + -1
getarray powtwo i k
kd = k * D
if N < kd jump next_iter
q = q + k
kdneg = kd * -1
N = N + kdneg
next_iter:
if i jump loop_start
return q N
endfunc
