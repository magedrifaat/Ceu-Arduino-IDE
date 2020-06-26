# SPI (Serial Peripheral Interface)

## API

### Includes

```
#include "spi.ceu"
```

### Code Abstractions

#### SPI_Transaction

Sets up an SPI transaction with the given parameters.

```
code/await SPI_Transaction (var u32 freq, var u8 bit_order, var u8 mode, var int? cs, var int? csn) -> NEVER;
```

Parameters:

- `u32`:  transmission frequency
- `u8`:   byte order (`SPI_LSBFIRST`, `SPI_MSBFIRST`)
- `u8`:   mode of operation (`SPI_MODE0`, `SPI_MODE1`, `SPI_MODE2`, `SPI_MODE3`)
- `int?`: chip select `cs`  pin (active `high`)
- `int?`: chip select `csn` pin (active `low`)

The chip select pins are optional, but at most one may be set.

Return:

- `NEVER`: never returns

SPI transactions cannot be concurrent and must be serialized accordingly.

#### SPI_Transfer

Transmits and receives a vector of bytes.

```
code/await SPI_Transfer (var&[] byte buf) -> none;
```

Parameters:

- `&[] byte`: reference to single buffer to transfer and receive in place

Return:

- `none`

Transfers must be always enclosed in [transactions](#spi_transaction).

#### SPI_Transfer_8

Transmits and receives a byte.

```
code/await SPI_Transfer_8 (var byte? v) -> byte;
```

Parameters:

- `byte?`: value to transmit to the peripheral (default: *don't care*)

Return:

- `byte`: value received from the peripheral

Transfers must be always enclosed in [transactions](#spi_transaction).

### Examples

`TODO`
