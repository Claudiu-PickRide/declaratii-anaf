/*
This file is part of DUKIntegrator.

DUKIntegrator is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

DUKIntegrator is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with DUKIntegrator.  If not, see <http://www.gnu.org/licenses/>.
 */
package ui;

import java.util.List;
import pdf.Sign.CertAlias;

public interface CertificateChooser
{
    public CertAlias chooseCertificate(List col);
    public String chooseZipFile(String xmlFile, int zipOption);
}
