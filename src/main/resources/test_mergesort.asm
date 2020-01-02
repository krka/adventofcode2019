#include mergesort.asm

int i = 0
int N = 0
int tmp = 0
array[1000] array
array[1000] tempArray

N = input()

read_input:
tmp = input()
array[i] = tmp
i = i + 1
if i < N jump read_input

mergeSortInner(0, N, array, tempArray)

i = 0
emit:
tmp = array[i]
output(tmp)
i = i + 1
if i < N jump emit

halt
