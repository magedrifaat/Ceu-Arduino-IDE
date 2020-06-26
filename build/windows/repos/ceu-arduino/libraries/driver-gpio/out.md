# Digital Output

Output digital pins are writable as `high` or `low`.

## API

### Includes

```
#include "out.ceu"
```

### Outputs

#### OUT_XX

Sets the state of an individual output pin.

```
output high/low OUT_XX;     // `XX` is a number, e.g., `OUT_13`
```

Parameters:

1. `high/low`: new state of the pin

#### OUT

Sets the state of a parameterized output pin.

```
output (int, high/low) OUT;
```

Parameters:

1. `int`:      pin to change
2. `high/low`: new state of the pin

The individual pins to use must also be declared with [`OUT_XX`](#out_xx) for
proper configuration.

## Examples

### Individual output

Writes `high` to pin 13:

```
#include "out.ceu"

output high/low OUT_13;

emit OUT_13(high);

await FOREVER;
```

### Parameterized output

Writes `high` to pins 10-13:

```
#include "out.ceu"

output high/low OUT_10;
output high/low OUT_11;
output high/low OUT_12;
output high/low OUT_13;

var int i;
loop i in [10 -> 13] do
    emit OUT(i, high);
end

await FOREVER;
```
