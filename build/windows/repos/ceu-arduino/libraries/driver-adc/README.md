# ADC (Analog to Digital Converter)

Analog pins can read analog voltages and convert to 10-bit digital values (`0`
to `1023`).

## API

### Includes

```
#include "adc.ceu"
```

### Code Abstractions

#### Adc

Initializes the ADC peripheral.

```
code/await Adc (none) -> NEVER;
```

Parameters:

- `none`

Return:

- `NEVER`: never returns

#### Adc_Conversion

Requests an analog to digital conversion.

```
code/await Adc_Conversion (var int pin) -> int;
```

Parameters:

1. `int`: pin to read

Return:

- `int`: value of the conversion

## Examples

### Periodic conversion

Reads `_A0` every second and sets `OUT_13` if the value is greater than `1000`:

```
#include "out.ceu"
#include "wclock.ceu"
#include "adc.ceu"

output high/low OUT_13;

spawn Adc();

loop do
    await 1s;
    var int value = await Adc_Conversion(_A0);
    emit OUT_13(value > 1000);
end
```

### Concurrent conversion

Reads `_A0` and `_A1` every second and sets `OUT_13` if the sum is greater than
`2000`:

```
#include "out.ceu"
#include "wclock.ceu"
#include "adc.ceu"

output high/low OUT_13;

spawn Adc();

loop do
    await 1s;
    var int v1 = _;
    var int v2 = _;
    par/and do
        v1 = await Adc_Conversion(_A0);
    with
        v2 = await Adc_Conversion(_A1);
    end
    if v1+v2 > 2000 then
        emit OUT_13(high);
    else
        emit OUT_13(low);
    end
end
```
