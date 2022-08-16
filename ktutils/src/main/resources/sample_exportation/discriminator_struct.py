# D (Discriminator)
# CNN (Convolutional Neural Network)
# Resize convolution

from torch import nn

self = self
ir = self.config["image_resolution"]
ic = self.config["image_channel_count"]
lr = self.config["label_resolution"]
lc = self.config["label_channel_count"]
fm = self.config["feature_map_count"]

# NOTE:
# nn.Conv2d positional params: in_channels, out_channels, kernel_size, stride, padding
# nn.Upsample positional params: size
# nn.LeakyReLU positional params: negative_slope, inplace
# nn.BatchNorm2d positional params: num_features

_Conv2d = nn.Conv2d
_Upsample = nn.Upsample
_LeakyReLU = nn.LeakyReLU
_BatchNorm2d = nn.BatchNorm2d
_Sigmoid = nn.Sigmoid

self.model = nn.Sequential(
    # Layer group 1. input group; scale 1 / 2.52
    _Conv2d(ic, fm, 5, 1, 2, bias=False),
    _Upsample(int(ir / 2.52), mode="bicubic", align_corners=False),
    _BatchNorm2d(fm),
    _LeakyReLU(0.2, True),
    # 2. scale 1 / 6.35 ~= 1 / (2.52)^2
    _Conv2d(fm, int(4 * fm), 3, 1, 1, bias=False),
    _Upsample(int(ir / 6.35), mode="bilinear", align_corners=False),
    _BatchNorm2d(int(4 * fm)),
    _LeakyReLU(0.2, True),
    # 3. scale 1 / 16 ~= 1 / (2.52)^3
    _Conv2d(int(4 * fm), int(8 * fm), 3, 1, 1, bias=False),
    _Upsample(4, mode="bilinear", align_corners=False),
    _BatchNorm2d(int(8 * fm)),
    _LeakyReLU(0.2, True),
    # 4. output group
    _Conv2d(int(8 * fm), lc, 3, 1, 1, bias=False),
    _Upsample(lr, mode="bicubic", align_corners=False),
    _Sigmoid()
)
