/*
    Copyright 2017-2023 Will Winder

    This file is part of Universal Gcode Sender (UGS).

    UGS is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    UGS is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with UGS.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.willwinder.ugs.platform.probe.renderable;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.util.gl2.GLUT;
import com.willwinder.ugs.nbm.visualizer.options.VisualizerOptions;
import com.willwinder.ugs.nbm.visualizer.shared.Renderable;
import com.willwinder.ugs.platform.probe.ProbeParameters;
import com.willwinder.ugs.platform.probe.ProbeSettings;
import com.willwinder.universalgcodesender.i18n.Localization;
import com.willwinder.universalgcodesender.model.Position;

import static com.willwinder.ugs.nbm.visualizer.options.VisualizerOptions.VISUALIZER_OPTION_PROBE_PREVIEW;
import com.willwinder.ugs.platform.probe.renderable.ProbeRenderableHelpers.Triangle;
import static com.willwinder.ugs.platform.probe.renderable.ProbeRenderableHelpers.drawArrow;

/**
 *
 * @author risototh
 */
public class HoleCenterPathPreview extends AbstractProbePreview
{
    private Position startWork = null;
    private ProbeParameters pc = null;
    private double hcDiameter = 0;

    private final GLUT glut;

    private final Triangle[] stlImportedHoleModel = {
        new Triangle(-4.928080e-01, -8.450041e-02, 1.000000e+00, -4.928080e-01, -8.450041e-02, 0.000000e+00, -5.000000e-01, -6.123234e-17, 1.000000e+00),
        new Triangle(-5.000000e-01, -6.123234e-17, 1.000000e+00, -4.928080e-01, -8.450041e-02, 0.000000e+00, -5.000000e-01, -6.123234e-17, 0.000000e+00),
        new Triangle(-5.000000e-01, -6.123234e-17, 1.000000e+00, -5.000000e-01, -6.123234e-17, 0.000000e+00, -4.928080e-01, 8.450041e-02, 1.000000e+00),
        new Triangle(-4.928080e-01, 8.450041e-02, 1.000000e+00, -5.000000e-01, -6.123234e-17, 0.000000e+00, -4.928080e-01, 8.450041e-02, 0.000000e+00),
        new Triangle(-4.928080e-01, 8.450041e-02, 1.000000e+00, -4.928080e-01, 8.450041e-02, 0.000000e+00, -4.714387e-01, 1.665699e-01, 1.000000e+00),
        new Triangle(-4.714387e-01, 1.665699e-01, 1.000000e+00, -4.928080e-01, 8.450041e-02, 0.000000e+00, -4.714387e-01, 1.665699e-01, 0.000000e+00),
        new Triangle(-4.714387e-01, 1.665699e-01, 1.000000e+00, -4.714387e-01, 1.665699e-01, 0.000000e+00, -4.365071e-01, 2.438475e-01, 1.000000e+00),
        new Triangle(-4.365071e-01, 2.438475e-01, 1.000000e+00, -4.714387e-01, 1.665699e-01, 0.000000e+00, -4.365071e-01, 2.438475e-01, 0.000000e+00),
        new Triangle(-4.365071e-01, 2.438475e-01, 1.000000e+00, -4.365071e-01, 2.438475e-01, 0.000000e+00, -3.890179e-01, 3.141100e-01, 1.000000e+00),
        new Triangle(-3.890179e-01, 3.141100e-01, 1.000000e+00, -4.365071e-01, 2.438475e-01, 0.000000e+00, -3.890179e-01, 3.141100e-01, 0.000000e+00),
        new Triangle(-3.890179e-01, 3.141100e-01, 1.000000e+00, -3.890179e-01, 3.141100e-01, 0.000000e+00, -3.303374e-01, 3.753362e-01, 1.000000e+00),
        new Triangle(-3.303374e-01, 3.753362e-01, 1.000000e+00, -3.890179e-01, 3.141100e-01, 0.000000e+00, -3.303374e-01, 3.753362e-01, 0.000000e+00),
        new Triangle(-3.303374e-01, 3.753362e-01, 1.000000e+00, -3.303374e-01, 3.753362e-01, 0.000000e+00, -2.621536e-01, 4.257646e-01, 1.000000e+00),
        new Triangle(-2.621536e-01, 4.257646e-01, 1.000000e+00, -3.303374e-01, 3.753362e-01, 0.000000e+00, -2.621536e-01, 4.257646e-01, 0.000000e+00),
        new Triangle(-2.621536e-01, 4.257646e-01, 1.000000e+00, -2.621536e-01, 4.257646e-01, 0.000000e+00, -1.864282e-01, 4.639445e-01, 1.000000e+00),
        new Triangle(-1.864282e-01, 4.639445e-01, 1.000000e+00, -2.621536e-01, 4.257646e-01, 0.000000e+00, -1.864282e-01, 4.639445e-01, 0.000000e+00),
        new Triangle(-1.864282e-01, 4.639445e-01, 1.000000e+00, -1.864282e-01, 4.639445e-01, 0.000000e+00, -1.053396e-01, 4.887776e-01, 1.000000e+00),
        new Triangle(-1.053396e-01, 4.887776e-01, 1.000000e+00, -1.864282e-01, 4.639445e-01, 0.000000e+00, -1.053396e-01, 4.887776e-01, 0.000000e+00),
        new Triangle(-1.053396e-01, 4.887776e-01, 1.000000e+00, -1.053396e-01, 4.887776e-01, 0.000000e+00, -2.122060e-02, 4.995495e-01, 1.000000e+00),
        new Triangle(-2.122060e-02, 4.995495e-01, 1.000000e+00, -1.053396e-01, 4.887776e-01, 0.000000e+00, -2.122060e-02, 4.995495e-01, 0.000000e+00),
        new Triangle(-2.122060e-02, 4.995495e-01, 1.000000e+00, -2.122060e-02, 4.995495e-01, 0.000000e+00, 6.350891e-02, 4.959502e-01, 1.000000e+00),
        new Triangle(6.350891e-02, 4.959502e-01, 1.000000e+00, -2.122060e-02, 4.995495e-01, 0.000000e+00, 6.350891e-02, 4.959502e-01, 0.000000e+00),
        new Triangle(6.350891e-02, 4.959502e-01, 1.000000e+00, 6.350891e-02, 4.959502e-01, 0.000000e+00, 1.464114e-01, 4.780834e-01, 1.000000e+00),
        new Triangle(1.464114e-01, 4.780834e-01, 1.000000e+00, 6.350891e-02, 4.959502e-01, 0.000000e+00, 1.464114e-01, 4.780834e-01, 0.000000e+00),
        new Triangle(1.464114e-01, 4.780834e-01, 1.000000e+00, 1.464114e-01, 4.780834e-01, 0.000000e+00, 2.251019e-01, 4.464629e-01, 1.000000e+00),
        new Triangle(2.251019e-01, 4.464629e-01, 1.000000e+00, 1.464114e-01, 4.780834e-01, 0.000000e+00, 2.251019e-01, 4.464629e-01, 0.000000e+00),
        new Triangle(2.251019e-01, 4.464629e-01, 1.000000e+00, 2.251019e-01, 4.464629e-01, 0.000000e+00, 2.973166e-01, 4.019986e-01, 1.000000e+00),
        new Triangle(2.973166e-01, 4.019986e-01, 1.000000e+00, 2.251019e-01, 4.464629e-01, 0.000000e+00, 2.973166e-01, 4.019986e-01, 0.000000e+00),
        new Triangle(2.973166e-01, 4.019986e-01, 1.000000e+00, 2.973166e-01, 4.019986e-01, 0.000000e+00, 3.609780e-01, 3.459694e-01, 1.000000e+00),
        new Triangle(3.609780e-01, 3.459694e-01, 1.000000e+00, 2.973166e-01, 4.019986e-01, 0.000000e+00, 3.609780e-01, 3.459694e-01, 0.000000e+00),
        new Triangle(3.609780e-01, 3.459694e-01, 1.000000e+00, 3.609780e-01, 3.459694e-01, 0.000000e+00, 4.142548e-01, 2.799874e-01, 1.000000e+00),
        new Triangle(4.142548e-01, 2.799874e-01, 1.000000e+00, 3.609780e-01, 3.459694e-01, 0.000000e+00, 4.142548e-01, 2.799874e-01, 0.000000e+00),
        new Triangle(4.142548e-01, 2.799874e-01, 1.000000e+00, 4.142548e-01, 2.799874e-01, 0.000000e+00, 4.556142e-01, 2.059506e-01, 1.000000e+00),
        new Triangle(4.556142e-01, 2.059506e-01, 1.000000e+00, 4.142548e-01, 2.799874e-01, 0.000000e+00, 4.556142e-01, 2.059506e-01, 0.000000e+00),
        new Triangle(4.556142e-01, 2.059506e-01, 1.000000e+00, 4.556142e-01, 2.059506e-01, 0.000000e+00, 4.838665e-01, 1.259890e-01, 1.000000e+00),
        new Triangle(4.838665e-01, 1.259890e-01, 1.000000e+00, 4.556142e-01, 2.059506e-01, 0.000000e+00, 4.838665e-01, 1.259890e-01, 0.000000e+00),
        new Triangle(4.838665e-01, 1.259890e-01, 1.000000e+00, 4.838665e-01, 1.259890e-01, 0.000000e+00, 4.981987e-01, 4.240296e-02, 1.000000e+00),
        new Triangle(4.981987e-01, 4.240296e-02, 1.000000e+00, 4.838665e-01, 1.259890e-01, 0.000000e+00, 4.981987e-01, 4.240296e-02, 0.000000e+00),
        new Triangle(4.981987e-01, 4.240296e-02, 1.000000e+00, 4.981987e-01, 4.240296e-02, 0.000000e+00, 4.981987e-01, -4.240296e-02, 1.000000e+00),
        new Triangle(4.981987e-01, -4.240296e-02, 1.000000e+00, 4.981987e-01, 4.240296e-02, 0.000000e+00, 4.981987e-01, -4.240296e-02, 0.000000e+00),
        new Triangle(4.981987e-01, -4.240296e-02, 1.000000e+00, 4.981987e-01, -4.240296e-02, 0.000000e+00, 4.838665e-01, -1.259890e-01, 1.000000e+00),
        new Triangle(4.838665e-01, -1.259890e-01, 1.000000e+00, 4.981987e-01, -4.240296e-02, 0.000000e+00, 4.838665e-01, -1.259890e-01, 0.000000e+00),
        new Triangle(4.838665e-01, -1.259890e-01, 1.000000e+00, 4.838665e-01, -1.259890e-01, 0.000000e+00, 4.556142e-01, -2.059506e-01, 1.000000e+00),
        new Triangle(4.556142e-01, -2.059506e-01, 1.000000e+00, 4.838665e-01, -1.259890e-01, 0.000000e+00, 4.556142e-01, -2.059506e-01, 0.000000e+00),
        new Triangle(4.556142e-01, -2.059506e-01, 1.000000e+00, 4.556142e-01, -2.059506e-01, 0.000000e+00, 4.142548e-01, -2.799874e-01, 1.000000e+00),
        new Triangle(4.142548e-01, -2.799874e-01, 1.000000e+00, 4.556142e-01, -2.059506e-01, 0.000000e+00, 4.142548e-01, -2.799874e-01, 0.000000e+00),
        new Triangle(4.142548e-01, -2.799874e-01, 1.000000e+00, 4.142548e-01, -2.799874e-01, 0.000000e+00, 3.609780e-01, -3.459694e-01, 1.000000e+00),
        new Triangle(3.609780e-01, -3.459694e-01, 1.000000e+00, 4.142548e-01, -2.799874e-01, 0.000000e+00, 3.609780e-01, -3.459694e-01, 0.000000e+00),
        new Triangle(3.609780e-01, -3.459694e-01, 1.000000e+00, 3.609780e-01, -3.459694e-01, 0.000000e+00, 2.973166e-01, -4.019986e-01, 1.000000e+00),
        new Triangle(2.973166e-01, -4.019986e-01, 1.000000e+00, 3.609780e-01, -3.459694e-01, 0.000000e+00, 2.973166e-01, -4.019986e-01, 0.000000e+00),
        new Triangle(2.973166e-01, -4.019986e-01, 1.000000e+00, 2.973166e-01, -4.019986e-01, 0.000000e+00, 2.251019e-01, -4.464629e-01, 1.000000e+00),
        new Triangle(2.251019e-01, -4.464629e-01, 1.000000e+00, 2.973166e-01, -4.019986e-01, 0.000000e+00, 2.251019e-01, -4.464629e-01, 0.000000e+00),
        new Triangle(2.251019e-01, -4.464629e-01, 1.000000e+00, 2.251019e-01, -4.464629e-01, 0.000000e+00, 1.464114e-01, -4.780834e-01, 1.000000e+00),
        new Triangle(1.464114e-01, -4.780834e-01, 1.000000e+00, 2.251019e-01, -4.464629e-01, 0.000000e+00, 1.464114e-01, -4.780834e-01, 0.000000e+00),
        new Triangle(1.464114e-01, -4.780834e-01, 1.000000e+00, 1.464114e-01, -4.780834e-01, 0.000000e+00, 6.350891e-02, -4.959502e-01, 1.000000e+00),
        new Triangle(6.350891e-02, -4.959502e-01, 1.000000e+00, 1.464114e-01, -4.780834e-01, 0.000000e+00, 6.350891e-02, -4.959502e-01, 0.000000e+00),
        new Triangle(6.350891e-02, -4.959502e-01, 1.000000e+00, 6.350891e-02, -4.959502e-01, 0.000000e+00, -2.122060e-02, -4.995495e-01, 1.000000e+00),
        new Triangle(-2.122060e-02, -4.995495e-01, 1.000000e+00, 6.350891e-02, -4.959502e-01, 0.000000e+00, -2.122060e-02, -4.995495e-01, 0.000000e+00),
        new Triangle(-2.122060e-02, -4.995495e-01, 1.000000e+00, -2.122060e-02, -4.995495e-01, 0.000000e+00, -1.053396e-01, -4.887776e-01, 1.000000e+00),
        new Triangle(-1.053396e-01, -4.887776e-01, 1.000000e+00, -2.122060e-02, -4.995495e-01, 0.000000e+00, -1.053396e-01, -4.887776e-01, 0.000000e+00),
        new Triangle(-1.053396e-01, -4.887776e-01, 1.000000e+00, -1.053396e-01, -4.887776e-01, 0.000000e+00, -1.864282e-01, -4.639445e-01, 1.000000e+00),
        new Triangle(-1.864282e-01, -4.639445e-01, 1.000000e+00, -1.053396e-01, -4.887776e-01, 0.000000e+00, -1.864282e-01, -4.639445e-01, 0.000000e+00),
        new Triangle(-1.864282e-01, -4.639445e-01, 1.000000e+00, -1.864282e-01, -4.639445e-01, 0.000000e+00, -2.621536e-01, -4.257646e-01, 1.000000e+00),
        new Triangle(-2.621536e-01, -4.257646e-01, 1.000000e+00, -1.864282e-01, -4.639445e-01, 0.000000e+00, -2.621536e-01, -4.257646e-01, 0.000000e+00),
        new Triangle(-2.621536e-01, -4.257646e-01, 1.000000e+00, -2.621536e-01, -4.257646e-01, 0.000000e+00, -3.303374e-01, -3.753362e-01, 1.000000e+00),
        new Triangle(-3.303374e-01, -3.753362e-01, 1.000000e+00, -2.621536e-01, -4.257646e-01, 0.000000e+00, -3.303374e-01, -3.753362e-01, 0.000000e+00),
        new Triangle(-3.303374e-01, -3.753362e-01, 1.000000e+00, -3.303374e-01, -3.753362e-01, 0.000000e+00, -3.890179e-01, -3.141100e-01, 1.000000e+00),
        new Triangle(-3.890179e-01, -3.141100e-01, 1.000000e+00, -3.303374e-01, -3.753362e-01, 0.000000e+00, -3.890179e-01, -3.141100e-01, 0.000000e+00),
        new Triangle(-3.890179e-01, -3.141100e-01, 1.000000e+00, -3.890179e-01, -3.141100e-01, 0.000000e+00, -4.365071e-01, -2.438475e-01, 1.000000e+00),
        new Triangle(-4.365071e-01, -2.438475e-01, 1.000000e+00, -3.890179e-01, -3.141100e-01, 0.000000e+00, -4.365071e-01, -2.438475e-01, 0.000000e+00),
        new Triangle(-4.365071e-01, -2.438475e-01, 1.000000e+00, -4.365071e-01, -2.438475e-01, 0.000000e+00, -4.714387e-01, -1.665699e-01, 1.000000e+00),
        new Triangle(-4.714387e-01, -1.665699e-01, 1.000000e+00, -4.365071e-01, -2.438475e-01, 0.000000e+00, -4.714387e-01, -1.665699e-01, 0.000000e+00),
        new Triangle(-4.714387e-01, -1.665699e-01, 1.000000e+00, -4.714387e-01, -1.665699e-01, 0.000000e+00, -4.928080e-01, -8.450041e-02, 1.000000e+00),
        new Triangle(-4.928080e-01, -8.450041e-02, 1.000000e+00, -4.714387e-01, -1.665699e-01, 0.000000e+00, -4.928080e-01, -8.450041e-02, 0.000000e+00),
        new Triangle(-5.500000e-01, -5.500000e-01, 0.000000e+00, 5.500000e-01, -5.500000e-01, 0.000000e+00, -5.500000e-01, -5.500000e-01, 1.000000e+00),
        new Triangle(-5.500000e-01, -5.500000e-01, 1.000000e+00, 5.500000e-01, -5.500000e-01, 0.000000e+00, 5.500000e-01, -5.500000e-01, 1.000000e+00),
        new Triangle(-5.500000e-01, 5.500000e-01, 0.000000e+00, -5.500000e-01, -5.500000e-01, 0.000000e+00, -5.500000e-01, 5.500000e-01, 1.000000e+00),
        new Triangle(-5.500000e-01, 5.500000e-01, 1.000000e+00, -5.500000e-01, -5.500000e-01, 0.000000e+00, -5.500000e-01, -5.500000e-01, 1.000000e+00),
        new Triangle(5.500000e-01, 5.500000e-01, 0.000000e+00, -5.500000e-01, 5.500000e-01, 0.000000e+00, 5.500000e-01, 5.500000e-01, 1.000000e+00),
        new Triangle(5.500000e-01, 5.500000e-01, 1.000000e+00, -5.500000e-01, 5.500000e-01, 0.000000e+00, -5.500000e-01, 5.500000e-01, 1.000000e+00),
        new Triangle(5.500000e-01, -5.500000e-01, 0.000000e+00, 5.500000e-01, 5.500000e-01, 0.000000e+00, 5.500000e-01, -5.500000e-01, 1.000000e+00),
        new Triangle(5.500000e-01, -5.500000e-01, 1.000000e+00, 5.500000e-01, 5.500000e-01, 0.000000e+00, 5.500000e-01, 5.500000e-01, 1.000000e+00),
        new Triangle(-4.928080e-01, 8.450041e-02, 1.000000e+00, -5.500000e-01, 5.500000e-01, 1.000000e+00, -5.000000e-01, -6.123234e-17, 1.000000e+00),
        new Triangle(-5.000000e-01, -6.123234e-17, 1.000000e+00, -5.500000e-01, 5.500000e-01, 1.000000e+00, -5.500000e-01, -5.500000e-01, 1.000000e+00),
        new Triangle(-5.000000e-01, -6.123234e-17, 1.000000e+00, -5.500000e-01, -5.500000e-01, 1.000000e+00, -4.928080e-01, -8.450041e-02, 1.000000e+00),
        new Triangle(-4.928080e-01, -8.450041e-02, 1.000000e+00, -5.500000e-01, -5.500000e-01, 1.000000e+00, -4.714387e-01, -1.665699e-01, 1.000000e+00),
        new Triangle(-4.714387e-01, -1.665699e-01, 1.000000e+00, -5.500000e-01, -5.500000e-01, 1.000000e+00, -4.365071e-01, -2.438475e-01, 1.000000e+00),
        new Triangle(-4.365071e-01, -2.438475e-01, 1.000000e+00, -5.500000e-01, -5.500000e-01, 1.000000e+00, -3.890179e-01, -3.141100e-01, 1.000000e+00),
        new Triangle(-3.890179e-01, -3.141100e-01, 1.000000e+00, -5.500000e-01, -5.500000e-01, 1.000000e+00, -3.303374e-01, -3.753362e-01, 1.000000e+00),
        new Triangle(-3.303374e-01, -3.753362e-01, 1.000000e+00, -5.500000e-01, -5.500000e-01, 1.000000e+00, -2.621536e-01, -4.257646e-01, 1.000000e+00),
        new Triangle(-2.621536e-01, -4.257646e-01, 1.000000e+00, -5.500000e-01, -5.500000e-01, 1.000000e+00, -1.864282e-01, -4.639445e-01, 1.000000e+00),
        new Triangle(-1.864282e-01, -4.639445e-01, 1.000000e+00, -5.500000e-01, -5.500000e-01, 1.000000e+00, -1.053396e-01, -4.887776e-01, 1.000000e+00),
        new Triangle(-1.053396e-01, -4.887776e-01, 1.000000e+00, -5.500000e-01, -5.500000e-01, 1.000000e+00, -2.122060e-02, -4.995495e-01, 1.000000e+00),
        new Triangle(-2.122060e-02, -4.995495e-01, 1.000000e+00, -5.500000e-01, -5.500000e-01, 1.000000e+00, 5.500000e-01, -5.500000e-01, 1.000000e+00),
        new Triangle(-2.122060e-02, -4.995495e-01, 1.000000e+00, 5.500000e-01, -5.500000e-01, 1.000000e+00, 6.350891e-02, -4.959502e-01, 1.000000e+00),
        new Triangle(6.350891e-02, -4.959502e-01, 1.000000e+00, 5.500000e-01, -5.500000e-01, 1.000000e+00, 1.464114e-01, -4.780834e-01, 1.000000e+00),
        new Triangle(1.464114e-01, -4.780834e-01, 1.000000e+00, 5.500000e-01, -5.500000e-01, 1.000000e+00, 2.251019e-01, -4.464629e-01, 1.000000e+00),
        new Triangle(2.251019e-01, -4.464629e-01, 1.000000e+00, 5.500000e-01, -5.500000e-01, 1.000000e+00, 2.973166e-01, -4.019986e-01, 1.000000e+00),
        new Triangle(2.973166e-01, -4.019986e-01, 1.000000e+00, 5.500000e-01, -5.500000e-01, 1.000000e+00, 3.609780e-01, -3.459694e-01, 1.000000e+00),
        new Triangle(3.609780e-01, -3.459694e-01, 1.000000e+00, 5.500000e-01, -5.500000e-01, 1.000000e+00, 4.142548e-01, -2.799874e-01, 1.000000e+00),
        new Triangle(4.142548e-01, -2.799874e-01, 1.000000e+00, 5.500000e-01, -5.500000e-01, 1.000000e+00, 4.556142e-01, -2.059506e-01, 1.000000e+00),
        new Triangle(4.556142e-01, -2.059506e-01, 1.000000e+00, 5.500000e-01, -5.500000e-01, 1.000000e+00, 4.838665e-01, -1.259890e-01, 1.000000e+00),
        new Triangle(4.838665e-01, -1.259890e-01, 1.000000e+00, 5.500000e-01, -5.500000e-01, 1.000000e+00, 4.981987e-01, -4.240296e-02, 1.000000e+00),
        new Triangle(4.981987e-01, -4.240296e-02, 1.000000e+00, 5.500000e-01, -5.500000e-01, 1.000000e+00, 4.981987e-01, 4.240296e-02, 1.000000e+00),
        new Triangle(4.981987e-01, 4.240296e-02, 1.000000e+00, 5.500000e-01, -5.500000e-01, 1.000000e+00, 5.500000e-01, 5.500000e-01, 1.000000e+00),
        new Triangle(4.981987e-01, 4.240296e-02, 1.000000e+00, 5.500000e-01, 5.500000e-01, 1.000000e+00, 4.838665e-01, 1.259890e-01, 1.000000e+00),
        new Triangle(4.838665e-01, 1.259890e-01, 1.000000e+00, 5.500000e-01, 5.500000e-01, 1.000000e+00, 4.556142e-01, 2.059506e-01, 1.000000e+00),
        new Triangle(4.556142e-01, 2.059506e-01, 1.000000e+00, 5.500000e-01, 5.500000e-01, 1.000000e+00, 4.142548e-01, 2.799874e-01, 1.000000e+00),
        new Triangle(4.142548e-01, 2.799874e-01, 1.000000e+00, 5.500000e-01, 5.500000e-01, 1.000000e+00, 3.609780e-01, 3.459694e-01, 1.000000e+00),
        new Triangle(3.609780e-01, 3.459694e-01, 1.000000e+00, 5.500000e-01, 5.500000e-01, 1.000000e+00, 2.973166e-01, 4.019986e-01, 1.000000e+00),
        new Triangle(2.973166e-01, 4.019986e-01, 1.000000e+00, 5.500000e-01, 5.500000e-01, 1.000000e+00, 2.251019e-01, 4.464629e-01, 1.000000e+00),
        new Triangle(2.251019e-01, 4.464629e-01, 1.000000e+00, 5.500000e-01, 5.500000e-01, 1.000000e+00, 1.464114e-01, 4.780834e-01, 1.000000e+00),
        new Triangle(1.464114e-01, 4.780834e-01, 1.000000e+00, 5.500000e-01, 5.500000e-01, 1.000000e+00, 6.350891e-02, 4.959502e-01, 1.000000e+00),
        new Triangle(6.350891e-02, 4.959502e-01, 1.000000e+00, 5.500000e-01, 5.500000e-01, 1.000000e+00, -2.122060e-02, 4.995495e-01, 1.000000e+00),
        new Triangle(-2.122060e-02, 4.995495e-01, 1.000000e+00, 5.500000e-01, 5.500000e-01, 1.000000e+00, -5.500000e-01, 5.500000e-01, 1.000000e+00),
        new Triangle(-2.122060e-02, 4.995495e-01, 1.000000e+00, -5.500000e-01, 5.500000e-01, 1.000000e+00, -1.053396e-01, 4.887776e-01, 1.000000e+00),
        new Triangle(-1.053396e-01, 4.887776e-01, 1.000000e+00, -5.500000e-01, 5.500000e-01, 1.000000e+00, -1.864282e-01, 4.639445e-01, 1.000000e+00),
        new Triangle(-1.864282e-01, 4.639445e-01, 1.000000e+00, -5.500000e-01, 5.500000e-01, 1.000000e+00, -2.621536e-01, 4.257646e-01, 1.000000e+00),
        new Triangle(-2.621536e-01, 4.257646e-01, 1.000000e+00, -5.500000e-01, 5.500000e-01, 1.000000e+00, -3.303374e-01, 3.753362e-01, 1.000000e+00),
        new Triangle(-3.303374e-01, 3.753362e-01, 1.000000e+00, -5.500000e-01, 5.500000e-01, 1.000000e+00, -3.890179e-01, 3.141100e-01, 1.000000e+00),
        new Triangle(-3.890179e-01, 3.141100e-01, 1.000000e+00, -5.500000e-01, 5.500000e-01, 1.000000e+00, -4.365071e-01, 2.438475e-01, 1.000000e+00),
        new Triangle(-4.365071e-01, 2.438475e-01, 1.000000e+00, -5.500000e-01, 5.500000e-01, 1.000000e+00, -4.714387e-01, 1.665699e-01, 1.000000e+00),
        new Triangle(-4.714387e-01, 1.665699e-01, 1.000000e+00, -5.500000e-01, 5.500000e-01, 1.000000e+00, -4.928080e-01, 8.450041e-02, 1.000000e+00),
        new Triangle(-4.928080e-01, -8.450041e-02, 0.000000e+00, -5.500000e-01, -5.500000e-01, 0.000000e+00, -5.000000e-01, -6.123234e-17, 0.000000e+00),
        new Triangle(-5.000000e-01, -6.123234e-17, 0.000000e+00, -5.500000e-01, -5.500000e-01, 0.000000e+00, -5.500000e-01, 5.500000e-01, 0.000000e+00),
        new Triangle(-5.000000e-01, -6.123234e-17, 0.000000e+00, -5.500000e-01, 5.500000e-01, 0.000000e+00, -4.928080e-01, 8.450041e-02, 0.000000e+00),
        new Triangle(-4.928080e-01, 8.450041e-02, 0.000000e+00, -5.500000e-01, 5.500000e-01, 0.000000e+00, -4.714387e-01, 1.665699e-01, 0.000000e+00),
        new Triangle(-4.714387e-01, 1.665699e-01, 0.000000e+00, -5.500000e-01, 5.500000e-01, 0.000000e+00, -4.365071e-01, 2.438475e-01, 0.000000e+00),
        new Triangle(-4.365071e-01, 2.438475e-01, 0.000000e+00, -5.500000e-01, 5.500000e-01, 0.000000e+00, -3.890179e-01, 3.141100e-01, 0.000000e+00),
        new Triangle(-3.890179e-01, 3.141100e-01, 0.000000e+00, -5.500000e-01, 5.500000e-01, 0.000000e+00, -3.303374e-01, 3.753362e-01, 0.000000e+00),
        new Triangle(-3.303374e-01, 3.753362e-01, 0.000000e+00, -5.500000e-01, 5.500000e-01, 0.000000e+00, -2.621536e-01, 4.257646e-01, 0.000000e+00),
        new Triangle(-2.621536e-01, 4.257646e-01, 0.000000e+00, -5.500000e-01, 5.500000e-01, 0.000000e+00, -1.864282e-01, 4.639445e-01, 0.000000e+00),
        new Triangle(-1.864282e-01, 4.639445e-01, 0.000000e+00, -5.500000e-01, 5.500000e-01, 0.000000e+00, -1.053396e-01, 4.887776e-01, 0.000000e+00),
        new Triangle(-1.053396e-01, 4.887776e-01, 0.000000e+00, -5.500000e-01, 5.500000e-01, 0.000000e+00, -2.122060e-02, 4.995495e-01, 0.000000e+00),
        new Triangle(-2.122060e-02, 4.995495e-01, 0.000000e+00, -5.500000e-01, 5.500000e-01, 0.000000e+00, 5.500000e-01, 5.500000e-01, 0.000000e+00),
        new Triangle(-2.122060e-02, 4.995495e-01, 0.000000e+00, 5.500000e-01, 5.500000e-01, 0.000000e+00, 6.350891e-02, 4.959502e-01, 0.000000e+00),
        new Triangle(6.350891e-02, 4.959502e-01, 0.000000e+00, 5.500000e-01, 5.500000e-01, 0.000000e+00, 1.464114e-01, 4.780834e-01, 0.000000e+00),
        new Triangle(1.464114e-01, 4.780834e-01, 0.000000e+00, 5.500000e-01, 5.500000e-01, 0.000000e+00, 2.251019e-01, 4.464629e-01, 0.000000e+00),
        new Triangle(2.251019e-01, 4.464629e-01, 0.000000e+00, 5.500000e-01, 5.500000e-01, 0.000000e+00, 2.973166e-01, 4.019986e-01, 0.000000e+00),
        new Triangle(2.973166e-01, 4.019986e-01, 0.000000e+00, 5.500000e-01, 5.500000e-01, 0.000000e+00, 3.609780e-01, 3.459694e-01, 0.000000e+00),
        new Triangle(3.609780e-01, 3.459694e-01, 0.000000e+00, 5.500000e-01, 5.500000e-01, 0.000000e+00, 4.142548e-01, 2.799874e-01, 0.000000e+00),
        new Triangle(4.142548e-01, 2.799874e-01, 0.000000e+00, 5.500000e-01, 5.500000e-01, 0.000000e+00, 4.556142e-01, 2.059506e-01, 0.000000e+00),
        new Triangle(4.556142e-01, 2.059506e-01, 0.000000e+00, 5.500000e-01, 5.500000e-01, 0.000000e+00, 4.838665e-01, 1.259890e-01, 0.000000e+00),
        new Triangle(4.838665e-01, 1.259890e-01, 0.000000e+00, 5.500000e-01, 5.500000e-01, 0.000000e+00, 4.981987e-01, 4.240296e-02, 0.000000e+00),
        new Triangle(4.981987e-01, 4.240296e-02, 0.000000e+00, 5.500000e-01, 5.500000e-01, 0.000000e+00, 5.500000e-01, -5.500000e-01, 0.000000e+00),
        new Triangle(4.981987e-01, 4.240296e-02, 0.000000e+00, 5.500000e-01, -5.500000e-01, 0.000000e+00, 4.981987e-01, -4.240296e-02, 0.000000e+00),
        new Triangle(4.981987e-01, -4.240296e-02, 0.000000e+00, 5.500000e-01, -5.500000e-01, 0.000000e+00, 4.838665e-01, -1.259890e-01, 0.000000e+00),
        new Triangle(4.838665e-01, -1.259890e-01, 0.000000e+00, 5.500000e-01, -5.500000e-01, 0.000000e+00, 4.556142e-01, -2.059506e-01, 0.000000e+00),
        new Triangle(4.556142e-01, -2.059506e-01, 0.000000e+00, 5.500000e-01, -5.500000e-01, 0.000000e+00, 4.142548e-01, -2.799874e-01, 0.000000e+00),
        new Triangle(4.142548e-01, -2.799874e-01, 0.000000e+00, 5.500000e-01, -5.500000e-01, 0.000000e+00, 3.609780e-01, -3.459694e-01, 0.000000e+00),
        new Triangle(3.609780e-01, -3.459694e-01, 0.000000e+00, 5.500000e-01, -5.500000e-01, 0.000000e+00, 2.973166e-01, -4.019986e-01, 0.000000e+00),
        new Triangle(2.973166e-01, -4.019986e-01, 0.000000e+00, 5.500000e-01, -5.500000e-01, 0.000000e+00, 2.251019e-01, -4.464629e-01, 0.000000e+00),
        new Triangle(2.251019e-01, -4.464629e-01, 0.000000e+00, 5.500000e-01, -5.500000e-01, 0.000000e+00, 1.464114e-01, -4.780834e-01, 0.000000e+00),
        new Triangle(1.464114e-01, -4.780834e-01, 0.000000e+00, 5.500000e-01, -5.500000e-01, 0.000000e+00, 6.350891e-02, -4.959502e-01, 0.000000e+00),
        new Triangle(6.350891e-02, -4.959502e-01, 0.000000e+00, 5.500000e-01, -5.500000e-01, 0.000000e+00, -2.122060e-02, -4.995495e-01, 0.000000e+00),
        new Triangle(-2.122060e-02, -4.995495e-01, 0.000000e+00, 5.500000e-01, -5.500000e-01, 0.000000e+00, -5.500000e-01, -5.500000e-01, 0.000000e+00),
        new Triangle(-2.122060e-02, -4.995495e-01, 0.000000e+00, -5.500000e-01, -5.500000e-01, 0.000000e+00, -1.053396e-01, -4.887776e-01, 0.000000e+00),
        new Triangle(-1.053396e-01, -4.887776e-01, 0.000000e+00, -5.500000e-01, -5.500000e-01, 0.000000e+00, -1.864282e-01, -4.639445e-01, 0.000000e+00),
        new Triangle(-1.864282e-01, -4.639445e-01, 0.000000e+00, -5.500000e-01, -5.500000e-01, 0.000000e+00, -2.621536e-01, -4.257646e-01, 0.000000e+00),
        new Triangle(-2.621536e-01, -4.257646e-01, 0.000000e+00, -5.500000e-01, -5.500000e-01, 0.000000e+00, -3.303374e-01, -3.753362e-01, 0.000000e+00),
        new Triangle(-3.303374e-01, -3.753362e-01, 0.000000e+00, -5.500000e-01, -5.500000e-01, 0.000000e+00, -3.890179e-01, -3.141100e-01, 0.000000e+00),
        new Triangle(-3.890179e-01, -3.141100e-01, 0.000000e+00, -5.500000e-01, -5.500000e-01, 0.000000e+00, -4.365071e-01, -2.438475e-01, 0.000000e+00),
        new Triangle(-4.365071e-01, -2.438475e-01, 0.000000e+00, -5.500000e-01, -5.500000e-01, 0.000000e+00, -4.714387e-01, -1.665699e-01, 0.000000e+00),
        new Triangle(-4.714387e-01, -1.665699e-01, 0.000000e+00, -5.500000e-01, -5.500000e-01, 0.000000e+00, -4.928080e-01, -8.450041e-02, 0.000000e+00),
    };

    public HoleCenterPathPreview() {
        super(10, Localization.getString("probe.visualizer.hole-center-preview"));
        glut = new GLUT();
        ProbeSettings.addPreferenceChangeListener(e -> this.hcDiameter = ProbeSettings.getHcDiameter());
    }

    public void setContext(ProbeParameters pc, Position startWork, Position startMachine) {
        this.pc = pc;
        this.startWork = startWork;
    }

    @Override
    public void updateSettings() {
        updateSpacing(ProbeSettings.getHcDiameter());
    }

    @Override
    public boolean isEnabled() {
        return VisualizerOptions.getBooleanOption(VISUALIZER_OPTION_PROBE_PREVIEW, true);
    }

    @Override
    public void setEnabled(boolean enabled) {
        VisualizerOptions.setBooleanOption(VISUALIZER_OPTION_PROBE_PREVIEW, enabled);
    }

    public void updateSpacing(double hcDiameter) {
        this.hcDiameter = hcDiameter;
    }

    @Override
    public boolean rotate() {
        return true;
    }

    @Override
    public boolean center() {
        return true;
    }

    @Override
    public void init(GLAutoDrawable drawable) {
    }

    @Override
    public void reloadPreferences(VisualizerOptions vo) {
    }

    private boolean invalidSettings() {
        return this.hcDiameter <= 0;
    }

    @Override
    public void draw(GLAutoDrawable drawable, boolean idle, Position machineCoord, Position workCoord, Position objectMin, Position objectMax, double scaleFactor, Position mouseWorldCoordinates, Position rotation) {
        double inset = 0.5;

        if (invalidSettings()) return;

        GL2 gl = drawable.getGL().getGL2();

        if (startWork != null && pc.endPosition == null && isProbeCycleActive()) {
            // The WCS is reset at the start of these operations.
            if (pc.startPosition != null) {
            }
            // Follow tool.
            else {
                gl.glTranslated(startWork.x, startWork.y, startWork.z);
            }
        }
        // Follow tool.
        else {
            gl.glTranslated(workCoord.x, workCoord.y, workCoord.z);
        }

        gl.glColor4d(.8, .8, .8, 1);
        drawHoleModel(gl, hcDiameter, hcDiameter / 4.0); // holeDepth is not imporant, so for now just calculate it

        // draw arrows
        gl.glColor4d(8., 0., 0., 1);

        // x probe arrows
        drawArrow(gl, glut,
                new Position(0, 0, 0),
                new Position(-0.5 * hcDiameter, 0, 0));
        drawArrow(gl, glut,
                new Position(-0.5 * hcDiameter, -inset, 0),
                new Position(+0.5 * hcDiameter, -inset, 0));
        drawArrow(gl, glut,
                new Position(+0.5 * hcDiameter, 0, 0),
                new Position(0, 0, 0));

        // y probe arrows
        drawArrow(gl, glut,
                new Position(0, 0, 0),
                new Position(0, -0.5 * hcDiameter, 0));
        drawArrow(gl, glut,
                new Position(inset, -0.5 * hcDiameter, 0),
                new Position(inset, +0.5 * hcDiameter, 0));
        drawArrow(gl, glut,
                new Position(0, +0.5 * hcDiameter, 0),
                new Position(0, 0, 0));
    }

    private void drawHoleModel(GL2 gl, double holeDiameter, double holeDepth) {
        gl.glEnable(GL2.GL_NORMALIZE);
        gl.glPushMatrix();
        gl.glTranslated(0, 0, -holeDepth/2); // move the model Z to middle
        gl.glScaled(holeDiameter, holeDiameter, holeDepth); // scale the model
        ProbeRenderableHelpers.drawTriangleSet(gl, stlImportedHoleModel);
        gl.glPopMatrix();
    }
}
