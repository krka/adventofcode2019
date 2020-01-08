#include mergesort.asm

int i = 0
int N = 0

N = input()
array[N] array

read_input:
array[i] = input()
i = i + 1
if i < N jump read_input

mergeSort(N, array)

i = 0
emit:
output(array[i])
i = i + 1
if i < N jump emit


