#include outputs.asm
#include stdlib.asm
int num_entries = read_int()

array[num_entries] values

for i = 0, i < num_entries do
  skip_non_int()
  values[i] = read_int()
end

func solve()
  for a = 0, a < num_entries do
    int av = values[a]
    for b = a + 1, b < num_entries do
      int bv = values[b]
      for c = b + 1, c < num_entries do
        int cv = values[c]
        if av + bv + cv == 2020 then
          return av * bv * cv
        end
      end
    end
  end
  return 0
end

outputNumber(solve())
output(10)
