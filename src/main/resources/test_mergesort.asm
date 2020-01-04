#include mergesort.asm

int i = 0
int N = 0
int tmp = 0

N = input()
array[N] array

read_input:
tmp = input()
array[i] = tmp
i = i + 1
if i < N jump read_input

mergeSort(N, array)

i = 0
emit:
tmp = array[i]
output(tmp)
i = i + 1
if i < N jump emit

halt
