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
cmp = i == 64
if not cmp jump setup_powtwo

func div N D

i = 0
kd = D
label find_range
i = i + 1
kd = kd * 2
cmp = N < kd
if not cmp jump find_range


q = 0
label loop_start
i = i + -1
getarray powtwo i k
kd = k * D
cmp = N < kd
if cmp jump next_iter
q = q + k
kdneg = kd * -1
N = N + kdneg
label next_iter
if i jump loop_start
return q N
endfunc
