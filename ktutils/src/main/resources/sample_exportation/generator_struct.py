# G (Generator)
# CNN (Convolutional Neural Network)
# Resize transposed convolution

from torch import nn

self = self
zr = self.config["noise_resolution"]
zc = self.config["noise_channel_count"]
ir = self.config["image_resolution"]
ic = self.config["image_channel_count"]
fm = self.config["feature_map_count"]

# NOTE:
# nn.ConvTranspose2d positional params: in_channels, out_channels, kernel_size, stride, padding
# nn.Upsample positional params: size
# nn.ReLU positional params: inplace
# nn.BatchNorm2d positional params: num_features

_ConvTranspose2d = nn.ConvTranspose2d
_Upsample = nn.Upsample
_LeakyReLU = nn.LeakyReLU
_BatchNorm2d = nn.BatchNorm2d
_Tanh = nn.Tanh

self.model = nn.Sequential(
    # Layer group 1. input group; scale 1 / 16 ~= 1 / (2.52)^3
    _Upsample(4, mode="bicubic", align_corners=False),
    _ConvTranspose2d(zc, int(8 * fm), 3, 1, 1, bias=False),
    _LeakyReLU(2e-3, True),
    # 2. scale 1 / 6.35 ~= 1 / (2.52)^2
    _Upsample(int(ir / 6.35), mode="bilinear", align_corners=False),
    _ConvTranspose2d(int(8 * fm), int(4 * fm), 3, 1, 1, bias=False),
    _BatchNorm2d(int(4 * fm)),
    _LeakyReLU(2e-3, True),
    # 3. scale 1 / 2.52
    _Upsample(int(ir / 2.52), mode="bilinear", align_corners=False),
    _ConvTranspose2d(int(4 * fm), fm, 3, 1, 1, bias=False),
    _BatchNorm2d(fm),
    _LeakyReLU(2e-3, True),
    # 4. output group
    _Upsample(ir, mode="bicubic", align_corners=False),
    _ConvTranspose2d(fm, ic, 5, 1, 2, bias=False),
    _Tanh()
)
