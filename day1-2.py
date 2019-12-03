import fileinput

def rec(n, acc):
  n2 = int(n / 3) - 2
  if n2 <= 0: return acc
  return rec(n2, acc + n2)

sum = 0
for line in fileinput.input():
  line = line.strip()
  if line != "":
    n = int(line)
    sum += rec(n, 0)

print sum

